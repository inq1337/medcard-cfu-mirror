package org.cfuv.medcard.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cfuv.medcard.api.repository.CardUserRepository;
import org.cfuv.medcard.dto.security.AuthRequest;
import org.cfuv.medcard.model.user.CardUser;
import org.cfuv.medcard.model.user.PrivilegeLevel;
import org.cfuv.medcard.model.user.UserStatus;
import org.cfuv.medcard.service.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CardUserRepository cardUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Test
    public void testRegistrationAndProfileAccess() throws Exception {
        String email = UUID.randomUUID() + "normal@example.com";
        String password = "password";
        createUser(email, password, UserStatus.ACTIVE);
        String token = jwtService.generateToken(email);

        testSuccessfulAuthentication(email, password, token);
        testInvalidRegistration(email, password);
        testUnauthorizedProfileAccess();
    }

    @Test
    public void testForbiddenAccess() throws Exception {
        String email = UUID.randomUUID() + "blocked@example.com";
        String password = "password";
        createUser(email, password, UserStatus.BLOCKED);

        testBlockedUserAuthentication(email, password);
    }

    private void createUser(String email, String password, UserStatus status) {
        CardUser cardUser = new CardUser();
        cardUser.setEmail(email);
        cardUser.setFirstname("test");
        cardUser.setSurname("test");
        cardUser.setPrivilegeLevel(PrivilegeLevel.BASIC);
        cardUser.setPassword(passwordEncoder.encode(password));
        cardUser.setStatus(status);

        cardUserRepository.save(cardUser);
    }

    private void testSuccessfulAuthentication(String email, String password, String token) throws Exception {
        AuthRequest request = new AuthRequest(email, password, true);
        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email));
    }

    private void testInvalidRegistration(String email, String password) throws Exception {
        AuthRequest invalidRequest = new AuthRequest("invalid@example.com", password, true);
        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    private void testUnauthorizedProfileAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/me"))
                .andExpect(status().isUnauthorized());
    }

    private void testBlockedUserAuthentication(String email, String password) throws Exception {
        AuthRequest request = new AuthRequest(email, password, true);
        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
