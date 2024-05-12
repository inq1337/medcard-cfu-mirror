package org.cfuv.medcard.service;

import lombok.RequiredArgsConstructor;
import org.cfuv.medcard.api.repository.AnalysisTemplateRepository;
import org.cfuv.medcard.api.service.AnalysisTemplateService;
import org.cfuv.medcard.api.service.CardUserService;
import org.cfuv.medcard.dto.AnalysisTemplateRequest;
import org.cfuv.medcard.dto.TemplateSimpleItem;
import org.cfuv.medcard.dto.filter.AnalysisTemplateFilter;
import org.cfuv.medcard.exception.ObjectNotFoundException;
import org.cfuv.medcard.model.AnalysisTemplate;
import org.cfuv.medcard.model.user.CardUser;
import org.cfuv.medcard.util.filter.DxUtil;
import org.cfuv.medcard.util.filter.RequestFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AnalysisTemplateServiceImpl implements AnalysisTemplateService {

    private final AnalysisTemplateRepository analysisTemplateRepository;
    private final CardUserService cardUserService;

    @Override
    public Page<AnalysisTemplate> getAll(String userEmail, AnalysisTemplateFilter filter, Pageable p) {
        List<RequestFilter> filters = DxUtil.mapStringToFilters(filter.filter());
        CardUser cardUser = cardUserService.loadByEmail(userEmail);
        return filters.isEmpty() ? analysisTemplateRepository.findAllByCardUserAndDeletedIsFalse(cardUser, p)
                : analysisTemplateRepository.findAllByCardUserAndFilters(cardUser, filters, p);
    }

    @Override
    public AnalysisTemplate create(String userEmail, AnalysisTemplateRequest request) {
        // TODO: make name unique for non-deleted items
        // TODO: refactor on AnalysisTemplateMapper.INSTANCE.toEntity
        AnalysisTemplate analysisTemplate = new AnalysisTemplate();
        analysisTemplate.setCardUser(cardUserService.loadByEmail(userEmail));
        return updateTemplate(request, analysisTemplate);
    }

    @Override
    public AnalysisTemplate update(String userEmail, AnalysisTemplateRequest request, Long id) {
        AnalysisTemplate analysisTemplate = loadByIdAndCardUser(id, userEmail);
        return updateTemplate(request, analysisTemplate);
    }

    private AnalysisTemplate updateTemplate(AnalysisTemplateRequest request, AnalysisTemplate analysisTemplate) {
        analysisTemplate.setName(request.name());
        analysisTemplate.setParameters(request.parameters() == null ? new ArrayList<>() : request.parameters());
        return analysisTemplateRepository.save(analysisTemplate);
    }

    @Override
    public AnalysisTemplate delete(String userEmail, Long id) {
        AnalysisTemplate analysisTemplate = loadByIdAndCardUser(id, userEmail);
        analysisTemplate.setDeleted(true);
        return analysisTemplateRepository.save(analysisTemplate);
    }

    @Override
    public AnalysisTemplate loadByIdAndCardUser(long id, String userEmail) {
        return analysisTemplateRepository.findByIdAndCardUserAndDeletedIsFalse(id, cardUserService.loadByEmail(userEmail))
                .orElseThrow(ObjectNotFoundException.supply(AnalysisTemplate.class, id));
    }

    @Override
    public AnalysisTemplate loadByNameAndCardUser(String templateName, String userEmail) {
        return analysisTemplateRepository.findByNameAndCardUserAndDeletedIsFalse(templateName, cardUserService.loadByEmail(userEmail))
                .orElseThrow(ObjectNotFoundException.supply(AnalysisTemplate.class, templateName));
    }

    @Override
    public List<TemplateSimpleItem> getTemplatesSimple(String userEmail) {
        return analysisTemplateRepository.findAllByCardUserAndDeletedIsFalse(cardUserService.loadByEmail(userEmail));
    }


}
