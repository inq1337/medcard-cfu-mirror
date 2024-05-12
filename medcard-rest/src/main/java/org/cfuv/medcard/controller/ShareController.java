package org.cfuv.medcard.controller;

import lombok.RequiredArgsConstructor;
import org.cfuv.medcard.api.service.AnalysisService;
import org.cfuv.medcard.api.service.ImageService;
import org.cfuv.medcard.api.service.ShareService;
import org.cfuv.medcard.dto.ShareDTO;
import org.cfuv.medcard.dto.page.AnalysisPageResponse;
import org.cfuv.medcard.dto.security.JWTResponse;
import org.cfuv.medcard.mapper.AnalysisMapper;
import org.cfuv.medcard.model.Analysis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ShareController {

    private final ShareService shareService;
    private final AnalysisService analysisService;
    private final ImageService imageService;

    @PostMapping("/share")
    public ResponseEntity<JWTResponse> create(Authentication authentication, @RequestBody ShareDTO shareDTO) {
        String token = shareService.createToken(authentication.getName(), shareDTO);
        return ResponseEntity.ok(new JWTResponse(token));
    }

    @GetMapping("/share/analysis")
    public ResponseEntity<AnalysisPageResponse> get(@RequestHeader("Shared-Token") String token,
                                                    Pageable p) {
        String userEmail = shareService.getEmailFromToken(token);
        ShareDTO filter = shareService.getFilterFromToken(token);
        Page<Analysis> all = analysisService.getAll(userEmail, filter, p);
        return ResponseEntity.ok(AnalysisMapper.INSTANCE.toPageResponse(all));
    }

    @GetMapping("/share/analysis/{id}/image/{fileName}")
    public ResponseEntity<byte[]> getImage(@RequestHeader("Shared-Token") String token,
                                           @PathVariable String id,
                                           @PathVariable String fileName) {
        String userEmail = shareService.getEmailFromToken(token);
        return ResponseEntity.ok(imageService.get(fileName));
    }

}
