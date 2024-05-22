package org.cfuv.medcard.api.service;

import org.cfuv.medcard.api.repository.AnalysisTemplateRepository;
import org.cfuv.medcard.api.repository.CardUserRepository;
import org.cfuv.medcard.dto.AnalysisTemplateRequest;
import org.cfuv.medcard.dto.TemplateSimpleItem;
import org.cfuv.medcard.dto.filter.AnalysisTemplateFilter;
import org.cfuv.medcard.model.AnalysisTemplate;
import org.cfuv.medcard.model.user.CardUser;
import org.cfuv.medcard.model.user.PrivilegeLevel;
import org.cfuv.medcard.model.user.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AnalysisTemplateServiceTest {

    @Autowired
    private AnalysisTemplateService analysisTemplateService;

    @Autowired
    private AnalysisTemplateRepository analysisTemplateRepository;

    @Autowired
    private CardUserRepository cardUserRepository;

    private CardUser cardUser;

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
    }

    @Test
    void testGetAll() {
        AnalysisTemplateFilter filter = new AnalysisTemplateFilter("name,contains,Template");
        Pageable pageable = PageRequest.of(0, 5);

        createAnalysisTemplate("Template1");
        createAnalysisTemplate("Template2");

        Page<AnalysisTemplate> result = analysisTemplateService.getAll(cardUser.getEmail(), filter, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testCreate() {
        AnalysisTemplateRequest request = new AnalysisTemplateRequest("Template1", List.of());

        AnalysisTemplate result = analysisTemplateService.create(cardUser.getEmail(), request);

        assertNotNull(result);
        assertEquals("Template1", result.getName());
        assertEquals(cardUser, result.getCardUser());
    }

    @Test
    void testUpdate() {
        AnalysisTemplate template = createAnalysisTemplate("Template1");

        AnalysisTemplateRequest request = new AnalysisTemplateRequest("TemplateUpdated", List.of());

        AnalysisTemplate result = analysisTemplateService.update(cardUser.getEmail(), request, template.getId());

        assertNotNull(result);
        assertEquals("TemplateUpdated", result.getName());
    }

    @Test
    void testDelete() {
        AnalysisTemplate template = createAnalysisTemplate("Template1");

        AnalysisTemplate result = analysisTemplateService.delete(cardUser.getEmail(), template.getId());

        assertNotNull(result);
        assertTrue(result.isDeleted());
    }

    @Test
    void testLoadByIdAndCardUser() {
        AnalysisTemplate template = createAnalysisTemplate("Template1");

        AnalysisTemplate result = analysisTemplateService.loadByIdAndCardUser(template.getId(), cardUser.getEmail());

        assertNotNull(result);
        assertEquals("Template1", result.getName());
    }

    @Test
    void testGetTemplatesSimple() {
        createAnalysisTemplate("Template1");

        List<TemplateSimpleItem> result = analysisTemplateService.getTemplatesSimple(cardUser.getEmail());

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCreateStartTemplates() {
        analysisTemplateService.createStartTemplates(cardUser);

        List<TemplateSimpleItem> templates = analysisTemplateRepository.findAllByCardUserAndDeletedIsFalse(cardUser);

        assertEquals(2, templates.size());
    }

    private AnalysisTemplate createAnalysisTemplate(String name) {
        AnalysisTemplate template = new AnalysisTemplate();
        template.setName(name);
        template.setCardUser(cardUser);
        template.setParameters(new ArrayList<>());
        template.setDeleted(false);
        analysisTemplateRepository.save(template);
        return template;
    }

}
