package org.cfuv.medcard.api.service;

import org.cfuv.medcard.dto.ShareDTO;
import org.cfuv.medcard.service.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ShareServiceTest {

    @Autowired
    private ShareService shareService;

    @Autowired
    private JwtService jwtService;

    private ShareDTO shareDTO;
    private String userEmail;
    private String token;

    @BeforeEach
    void setUp() {
        userEmail = "test@example.com";
        shareDTO = new ShareDTO(List.of(1L, 2L, 3L), LocalDate.of(2024, 1, 1));
        token = shareService.createToken(userEmail, shareDTO);
    }

    @Test
    void testCreateToken() {
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGetFilterFromToken() {
        ShareDTO result = shareService.getFilterFromToken(token);

        assertNotNull(result);
        assertEquals(shareDTO.ids(), result.ids());
        assertEquals(shareDTO.analysisDatesSince(), result.analysisDatesSince());
    }

    @Test
    void testGetEmailFromToken() {
        String result = shareService.getEmailFromToken(token);

        assertNotNull(result);
        assertEquals(userEmail, result);
    }

}
