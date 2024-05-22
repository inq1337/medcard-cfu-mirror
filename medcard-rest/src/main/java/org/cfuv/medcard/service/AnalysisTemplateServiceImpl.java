package org.cfuv.medcard.service;

import lombok.RequiredArgsConstructor;
import org.cfuv.medcard.api.repository.AnalysisTemplateRepository;
import org.cfuv.medcard.api.service.AnalysisTemplateService;
import org.cfuv.medcard.api.service.CardUserService;
import org.cfuv.medcard.dto.AnalysisTemplateRequest;
import org.cfuv.medcard.dto.TemplateSimpleItem;
import org.cfuv.medcard.dto.filter.AnalysisTemplateFilter;
import org.cfuv.medcard.exception.ObjectNotFoundException;
import org.cfuv.medcard.mapper.AnalysisTemplateMapper;
import org.cfuv.medcard.model.AnalysisTemplate;
import org.cfuv.medcard.model.parameter.TemplateParameter;
import org.cfuv.medcard.model.user.CardUser;
import org.cfuv.medcard.model.user.PrivilegeLevel;
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

    private static final int BASIC_ACCOUNT_TEMPLATE_COUNT = 3;
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
        CardUser cardUser = cardUserService.loadByEmail(userEmail);
        if (analysisTemplateRepository.countByCardUserAndDeletedIsFalse(cardUser) >= BASIC_ACCOUNT_TEMPLATE_COUNT &&
                !cardUser.getPrivilegeLevel().equals(PrivilegeLevel.PREMIUM)) {
            throw new RuntimeException("BASIC account can have only 3 templates");
        }
        checkNamesUniqueness(cardUser, request.name());
        return analysisTemplateRepository.save(AnalysisTemplateMapper.INSTANCE.toEntity(request, cardUser));
    }

    @Override
    public AnalysisTemplate update(String userEmail, AnalysisTemplateRequest request, Long id) {
        AnalysisTemplate analysisTemplate = loadByIdAndCardUser(id, userEmail);
        checkNamesUniqueness(cardUserService.loadByEmail(userEmail), request.name());
        return analysisTemplateRepository.save(AnalysisTemplateMapper.INSTANCE.partialUpdate(request, analysisTemplate));
    }

    private void checkNamesUniqueness(CardUser cardUser, String name) {
        if (analysisTemplateRepository.countByCardUserAndNameAndDeletedIsFalse(cardUser, name) != 0) {
            throw new RuntimeException("Names should be unique");
        }
    }

    @Override
    public AnalysisTemplate delete(String userEmail, Long id) {
        AnalysisTemplate analysisTemplate = loadByIdAndCardUser(id, userEmail);
        if (!"custom".equals(analysisTemplate.getName())) {
            analysisTemplate.setDeleted(true);
        } else {
            throw new RuntimeException("You can't delete custom template");
        }
        return analysisTemplateRepository.save(analysisTemplate);
    }

    @Override
    public AnalysisTemplate loadByIdAndCardUser(long id, String userEmail) {
        return analysisTemplateRepository.findByIdAndCardUserAndDeletedIsFalse(id, cardUserService.loadByEmail(userEmail))
                .orElseThrow(ObjectNotFoundException.supply(AnalysisTemplate.class, id));
    }

    @Override
    public List<TemplateSimpleItem> getTemplatesSimple(String userEmail) {
        return analysisTemplateRepository.findAllByCardUserAndDeletedIsFalse(cardUserService.loadByEmail(userEmail));
    }

    @Override
    public void createStartTemplates(CardUser cardUser) {
        analysisTemplateRepository.save(createCustomTemplate(cardUser));
        analysisTemplateRepository.save(createGBA(cardUser));
    }


    private AnalysisTemplate createGBA(CardUser cardUser) {
        List<TemplateParameter> parameters = List.of(
                new TemplateParameter("HGB (Гемоглобин)", "г/л", true),
                new TemplateParameter("RBC (Эритроциты)", "10^12/л", true),
                new TemplateParameter("WBC (Лейкоциты)", "10^9/л", true),
                new TemplateParameter("PLT (Тромбоциты)", "10^9/л", true),
                new TemplateParameter("HCT (Гематокрит)", "%", true),
                new TemplateParameter("MCV (Средний объем эритроцита)", "фл", true),
                new TemplateParameter("MCH (Среднее содержание гемоглобина в эритроците)", "пг", true),
                new TemplateParameter("MCHC (Средняя концентрация гемоглобина в эритроците)", "г/л", true),
                new TemplateParameter("RDW (Ширина распределения эритроцитов)", "%", true),
                new TemplateParameter("NEU (Нейтрофилы)", "%", true),
                new TemplateParameter("LYM (Лимфоциты)", "%", true),
                new TemplateParameter("MON (Моноциты)", "%", true),
                new TemplateParameter("EOS (Эозинофилы)", "%", true),
                new TemplateParameter("BAS (Базофилы)", "%", true),
                new TemplateParameter("ESR (СОЭ)", "мм/ч", true)
        );

        AnalysisTemplate gbaTemplate = new AnalysisTemplate();
        gbaTemplate.setName("ОАК (Общий анализ крови)");
        gbaTemplate.setCardUser(cardUser);
        gbaTemplate.setParameters(parameters);
        gbaTemplate.setDeleted(false);

        return gbaTemplate;
    }


    private AnalysisTemplate createCustomTemplate(CardUser cardUser) {
        AnalysisTemplate customTemplate = new AnalysisTemplate();
        customTemplate.setCardUser(cardUser);
        customTemplate.setName("custom");
        customTemplate.setParameters(new ArrayList<>());
        customTemplate.setDeleted(false);
        return customTemplate;
    }

}
