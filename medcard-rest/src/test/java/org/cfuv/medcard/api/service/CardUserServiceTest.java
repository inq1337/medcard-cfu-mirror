package org.cfuv.medcard.api.service;

import org.cfuv.medcard.api.repository.CardUserRepository;
import org.cfuv.medcard.dto.CardUserProfileDTO;
import org.cfuv.medcard.dto.ImageFileDTO;
import org.cfuv.medcard.model.user.CardUser;
import org.cfuv.medcard.model.user.PrivilegeLevel;
import org.cfuv.medcard.model.user.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CardUserServiceTest {

    @Autowired
    private CardUserService cardUserService;

    @Autowired
    private CardUserRepository cardUserRepository;

    @SpyBean
    private ImageService imageService;

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
    void testLoadByEmail() {
        CardUser loadedUser = cardUserService.loadByEmail(cardUser.getEmail());

        assertNotNull(loadedUser);
        assertEquals(cardUser.getEmail(), loadedUser.getEmail());
    }

    @Test
    void testSaveAvatar() {
        ImageFileDTO imageFileDTO = createImage();

        cardUserService.saveAvatar(cardUser.getEmail(), imageFileDTO);

        CardUser updatedUser = cardUserService.loadByEmail(cardUser.getEmail());
        assertNotNull(updatedUser.getAvatar());

        verify(imageService, times(1)).upload(imageFileDTO);
    }

    @Test
    void testGetAvatar() throws IOException {
        ImageFileDTO imageFileDTO = createImage();
        cardUserService.saveAvatar(cardUser.getEmail(), imageFileDTO);

        byte[] avatar = cardUserService.getAvatar(cardUser.getEmail());

        assertNotNull(avatar);
        assertArrayEquals(imageFileDTO.image().getBytes(), avatar);
    }

    @Test
    void testUpdateUserInfo() {
        CardUserProfileDTO profileDTO = new CardUserProfileDTO(cardUser.getEmail(), "newFirstName", "newSurname", null, null, PrivilegeLevel.PREMIUM);

        CardUser updatedUser = cardUserService.updateUserInfo(cardUser.getEmail(), profileDTO);

        assertNotNull(updatedUser);
        assertEquals("newFirstName", updatedUser.getFirstname());
        assertEquals("newSurname", updatedUser.getSurname());
        assertEquals(PrivilegeLevel.PREMIUM, updatedUser.getPrivilegeLevel());
    }

    private ImageFileDTO createImage() {
        MockMultipartFile mockFile = new MockMultipartFile("image", "image.jpg", "image/jpg", new byte[]{1, 2, 3});
        return new ImageFileDTO(mockFile);
    }

}
