package org.cfuv.medcard.controller;

import lombok.RequiredArgsConstructor;
import org.cfuv.medcard.api.service.CardUserService;
import org.cfuv.medcard.dto.CardUserProfileDTO;
import org.cfuv.medcard.dto.ImageFileDTO;
import org.cfuv.medcard.mapper.CardUserMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final CardUserService cardUserService;

    @GetMapping("/me")
    public ResponseEntity<CardUserProfileDTO> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(CardUserMapper.INSTANCE.toDTO(cardUserService.loadByEmail(authentication.getName())));
    }

    @PostMapping("/avatar")
    public ResponseEntity<Void> saveAvatar(Authentication authentication,
                                           @ModelAttribute ImageFileDTO imageFileDTO) {
        cardUserService.saveAvatar(authentication.getName(), imageFileDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/avatar")
    public ResponseEntity<byte[]> getAvatar(Authentication authentication) {
        byte[] avatar = cardUserService.getAvatar(authentication.getName());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(avatar);
    }

}
