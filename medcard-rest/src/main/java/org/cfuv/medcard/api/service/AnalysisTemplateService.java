package org.cfuv.medcard.api.service;

import org.cfuv.medcard.dto.AnalysisTemplateRequest;
import org.cfuv.medcard.dto.TemplateSimpleItem;
import org.cfuv.medcard.dto.filter.AnalysisTemplateFilter;
import org.cfuv.medcard.model.AnalysisTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnalysisTemplateService {

    Page<AnalysisTemplate> getAll(String userEmail, AnalysisTemplateFilter filter, Pageable p);

    AnalysisTemplate create(String userEmail, AnalysisTemplateRequest request);

    AnalysisTemplate update(String userEmail, AnalysisTemplateRequest request, Long id);

    AnalysisTemplate delete(String userEmail, Long id);

    AnalysisTemplate loadByIdAndCardUser(long id, String userEmail);

    AnalysisTemplate loadByNameAndCardUser(String templateName, String userEmail);

    List<TemplateSimpleItem> getTemplatesSimple(String userEmail);

}
