package org.cfuv.medcard.controller;

import lombok.RequiredArgsConstructor;
import org.cfuv.medcard.api.service.AnalysisTemplateService;
import org.cfuv.medcard.dto.AnalysisTemplateRequest;
import org.cfuv.medcard.dto.AnalysisTemplateResponse;
import org.cfuv.medcard.dto.TemplateSimpleItem;
import org.cfuv.medcard.dto.TemplatesListSimpleResponse;
import org.cfuv.medcard.dto.filter.AnalysisTemplateFilter;
import org.cfuv.medcard.dto.page.AnalysisTemplatePageResponse;
import org.cfuv.medcard.mapper.AnalysisTemplateMapper;
import org.cfuv.medcard.model.AnalysisTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
public class TemplateController {

    private final AnalysisTemplateService analysisTemplateService;

    @GetMapping("/template")
    public ResponseEntity<AnalysisTemplatePageResponse> get(Authentication authentication,
                                                            AnalysisTemplateFilter filter,
                                                            Pageable p) {
        Page<AnalysisTemplate> templates = analysisTemplateService.getAll(authentication.getName(), filter, p);
        return ResponseEntity.ok(AnalysisTemplateMapper.INSTANCE.toPageResponse(templates));
    }

    @PostMapping("/template")
    public ResponseEntity<AnalysisTemplateResponse> create(Authentication authentication,
                                                           @RequestBody AnalysisTemplateRequest request) {
        AnalysisTemplate createdTemplate = analysisTemplateService.create(authentication.getName(), request);
        return ResponseEntity.status(CREATED).body(AnalysisTemplateMapper.INSTANCE.toDTO(createdTemplate));
    }

    @PutMapping("/template/{id}")
    public ResponseEntity<AnalysisTemplateResponse> update(@PathVariable Long id,
                                                           Authentication authentication,
                                                           @RequestBody AnalysisTemplateRequest request) {
        AnalysisTemplate updatedTemplate = analysisTemplateService.update(authentication.getName(), request, id);
        return ResponseEntity.ok(AnalysisTemplateMapper.INSTANCE.toDTO(updatedTemplate));
    }

    @DeleteMapping("/template/{id}")
    public ResponseEntity<AnalysisTemplateResponse> delete(@PathVariable Long id,
                                                           Authentication authentication) {
        AnalysisTemplate deletedTemplate = analysisTemplateService.delete(authentication.getName(), id);
        return ResponseEntity.ok(AnalysisTemplateMapper.INSTANCE.toDTO(deletedTemplate));
    }

    @GetMapping("/template-list")
    public ResponseEntity<TemplatesListSimpleResponse> getTemplatesList(Authentication authentication) {
        List<TemplateSimpleItem> templatesSimple = analysisTemplateService.getTemplatesSimple(authentication.getName());
        return ResponseEntity.ok(new TemplatesListSimpleResponse(templatesSimple));
    }

}
