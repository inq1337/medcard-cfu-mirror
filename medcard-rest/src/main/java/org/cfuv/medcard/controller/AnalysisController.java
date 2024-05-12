package org.cfuv.medcard.controller;

import lombok.RequiredArgsConstructor;
import org.cfuv.medcard.api.service.AnalysisService;
import org.cfuv.medcard.dto.AnalysisRequest;
import org.cfuv.medcard.dto.AnalysisResponse;
import org.cfuv.medcard.dto.ImageDTO;
import org.cfuv.medcard.dto.ImageFileDTO;
import org.cfuv.medcard.dto.filter.AnalysisFilter;
import org.cfuv.medcard.dto.page.AnalysisPageResponse;
import org.cfuv.medcard.mapper.AnalysisMapper;
import org.cfuv.medcard.model.Analysis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
public class AnalysisController {

    private final AnalysisService analysisService;

    @GetMapping("/analysis")
    public ResponseEntity<AnalysisPageResponse> getAllAnalysis(Authentication authentication,
                                                               AnalysisFilter filter,
                                                               Pageable p) {
        Page<Analysis> all = analysisService.getAll(authentication.getName(), filter, p);
        return ResponseEntity.ok(AnalysisMapper.INSTANCE.toPageResponse(all));
    }

    @PostMapping("/analysis")
    public ResponseEntity<AnalysisResponse> create(Authentication authentication,
                                                   @RequestBody AnalysisRequest request) {
        Analysis created = analysisService.create(authentication.getName(), request);
        return ResponseEntity.status(CREATED).body(AnalysisMapper.INSTANCE.toDto(created));
    }

    @PutMapping("/analysis/{id}")
    public ResponseEntity<AnalysisResponse> update(@PathVariable Long id,
                                                   Authentication authentication,
                                                   @RequestBody AnalysisRequest request) {
        Analysis updated = analysisService.update(authentication.getName(), request, id);
        return ResponseEntity.ok(AnalysisMapper.INSTANCE.toDto(updated));
    }

    @DeleteMapping("/analysis/{id}")
    public ResponseEntity<AnalysisResponse> delete(@PathVariable Long id,
                                                   Authentication authentication) {
        Analysis deleted = analysisService.delete(authentication.getName(), id);
        return ResponseEntity.ok(AnalysisMapper.INSTANCE.toDto(deleted));
    }

    @PostMapping("/analysis/{id}/image")
    public ResponseEntity<ImageDTO> addImage(Authentication authentication,
                                             @PathVariable long id,
                                             @ModelAttribute ImageFileDTO imageFileDTO) {
        String imageName = analysisService.addImage(authentication.getName(), id, imageFileDTO);
        return ResponseEntity.ok(new ImageDTO(imageName));
    }

    @GetMapping("/analysis/{id}/image/{fileName}")
    public ResponseEntity<byte[]> getImage(Authentication authentication,
                                           @PathVariable long id,
                                           @PathVariable String fileName) {
        byte[] imageFile = analysisService.getImage(authentication.getName(), id, fileName);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageFile);
    }

    @DeleteMapping("/analysis/{id}/image/{fileName}")
    public ResponseEntity<Void> deleteImage(Authentication authentication,
                                            @PathVariable long id,
                                            @PathVariable String fileName) {
        analysisService.deleteImage(authentication.getName(), id, fileName);
        return ResponseEntity.ok().build();
    }

}
