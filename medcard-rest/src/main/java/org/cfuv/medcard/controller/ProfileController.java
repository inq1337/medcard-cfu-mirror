package org.cfuv.medcard.controller;

import lombok.RequiredArgsConstructor;
import org.cfuv.medcard.api.service.CardUserService;
import org.cfuv.medcard.dto.CardUserProfileDTO;
import org.cfuv.medcard.dto.ImageFileDTO;
import org.cfuv.medcard.mapper.CardUserMapper;
import org.cfuv.medcard.model.user.CardUser;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final CardUserService cardUserService;

    @GetMapping("/me")
    public ResponseEntity<CardUserProfileDTO> getCurrentUser(Authentication authentication) {
        CardUser cardUser = cardUserService.loadByEmail(authentication.getName());
        return ResponseEntity.ok(CardUserMapper.INSTANCE.toDto(cardUser));
    }

    @PutMapping("/me")
    public ResponseEntity<CardUserProfileDTO> updateCurrentUser(Authentication authentication,
                                                                @RequestBody CardUserProfileDTO profileDTO) {
        CardUser cardUser = cardUserService.updateUserInfo(authentication.getName(), profileDTO);
        return ResponseEntity.ok(CardUserMapper.INSTANCE.toDto(cardUser));
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

    @PostMapping("/premium")
    public ResponseEntity<Void> moveToPremium(Authentication authentication) {
        cardUserService.moveToPremium(authentication.getName());
        return ResponseEntity.ok().build();
    }

}
