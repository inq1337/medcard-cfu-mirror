package org.cfuv.medcard.api.service;

import org.cfuv.medcard.api.repository.AnalysisRepository;
import org.cfuv.medcard.api.repository.CardUserRepository;
import org.cfuv.medcard.dto.AnalysisRequest;
import org.cfuv.medcard.dto.AnalysisTemplateRequest;
import org.cfuv.medcard.dto.ImageFileDTO;
import org.cfuv.medcard.dto.ShareDTO;
import org.cfuv.medcard.dto.filter.AnalysisFilter;
import org.cfuv.medcard.model.Analysis;
import org.cfuv.medcard.model.AnalysisTemplate;
import org.cfuv.medcard.model.user.CardUser;
import org.cfuv.medcard.model.user.PrivilegeLevel;
import org.cfuv.medcard.model.user.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AnalysisServiceTest {

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private CardUserRepository cardUserRepository;

    @Autowired
    private AnalysisTemplateService analysisTemplateService;

    @SpyBean
    private ImageService imageService;

    private CardUser cardUser;
    private AnalysisTemplate analysisTemplate;

    @BeforeEach
    void setUp() {
        cardUser = new CardUser();
        String email = UUID.randomUUID() + "test@example.com";
        cardUser.setEmail(email);
        cardUser.setFirstname("test");
        cardUser.setSurname("test");
        cardUser.setPrivilegeLevel(PrivilegeLevel.BASIC);
        cardUser.setPassword("password");
        cardUser.setStatus(UserStatus.ACTIVE);

        cardUser = cardUserRepository.save(cardUser);

        analysisTemplate = analysisTemplateService.create(email, new AnalysisTemplateRequest("custom", new ArrayList<>()));
    }

    @Test
    void testGetAllWithFilter() {
        AnalysisFilter filter = new AnalysisFilter("name,contains,Analysis");
        Pageable pageable = PageRequest.of(0, 10);

        createAnalysis("Analysis1");

        createAnalysis("Analysis2");

        Page<Analysis> result = analysisService.getAll(cardUser.getEmail(), filter, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testGetAllWithShareDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDate analysisDate = LocalDate.of(2024, 1, 1);

        Analysis analysis = createAnalysis("Analysis1");
        analysis.setAnalysisDate(analysisDate);
        analysisRepository.save(analysis);

        ShareDTO filter = new ShareDTO(List.of(analysisTemplate.getId()), analysisDate);

        Page<Analysis> result = analysisService.getAll(cardUser.getEmail(), filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testCreate() {
        AnalysisRequest request = new AnalysisRequest("Analysis", analysisTemplate.getName(), analysisTemplate.getId(), LocalDate.now(), new ArrayList<>(), null);

        Analysis result = analysisService.create(cardUser.getEmail(), request);

        assertNotNull(result);
        assertEquals("Analysis", result.getName());
        assertEquals(cardUser, result.getCardUser());
    }

    @Test
    void testUpdate() {
        Analysis analysis = createAnalysis("Analysis1");

        AnalysisRequest request = new AnalysisRequest("UpdatedAnalysis", analysisTemplate.getName(), analysisTemplate.getId(), LocalDate.now(), new ArrayList<>(), null);

        Analysis result = analysisService.update(cardUser.getEmail(), request, analysis.getId());

        assertNotNull(result);
        assertEquals("UpdatedAnalysis", result.getName());
    }

    @Test
    void testDelete() {
        Analysis analysis = createAnalysis("Analysis1");
        Analysis result = analysisService.delete(cardUser.getEmail(), analysis.getId());

        assertNotNull(result);
        assertTrue(result.isDeleted());
    }

    @Test
    void testLoadByIdAndCardUser() {
        Analysis analysis = createAnalysis("Analysis1");
        Analysis result = analysisService.loadByIdAndCardUser(analysis.getId(), cardUser.getEmail());

        assertNotNull(result);
        assertEquals("Analysis1", result.getName());
    }

    @Test
    void testAddImage() {
        Analysis analysis = createAnalysis("Analysis1");

        ImageFileDTO imageFileDTO = createImage();

        String fileName = analysisService.addImage(cardUser.getEmail(), analysis.getId(), imageFileDTO);

        assertNotNull(fileName);
    }

    @Test
    void testGetImage() throws IOException {
        Analysis analysis = createAnalysis("Analysis1");
        ImageFileDTO imageFileDTO = createImage();
        String fileName = analysisService.addImage(cardUser.getEmail(), analysis.getId(), imageFileDTO);

        byte[] result = analysisService.getImage(fileName);

        assertArrayEquals(imageFileDTO.image().getBytes(), result);
    }

    @Test
    void testDeleteImage() {
        Analysis analysis = createAnalysis("Analysis1");
        ImageFileDTO imageFileDTO = createImage();
        String fileName = analysisService.addImage(cardUser.getEmail(), analysis.getId(), imageFileDTO);

        analysisService.deleteImage(cardUser.getEmail(), analysis.getId(), fileName);

        verify(imageService, times(1)).delete(fileName);
    }

    private ImageFileDTO createImage() {
        MockMultipartFile mockFile = new MockMultipartFile("image", "image.jpg", "image/jpg", new byte[]{1, 2, 3});
        return new ImageFileDTO(mockFile);
    }

    private Analysis createAnalysis(String name) {
        Analysis analysis = new Analysis();
        analysis.setName(name);
        analysis.setCardUser(cardUser);
        analysis.setTemplate(analysisTemplate);
        analysis.setAnalysisDate(LocalDate.now());
        analysis.setParameters(new ArrayList<>());
        analysis.setDeleted(false);
        return analysisRepository.save(analysis);
    }
}
