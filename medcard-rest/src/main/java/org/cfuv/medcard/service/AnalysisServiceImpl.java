package org.cfuv.medcard.service;

import lombok.RequiredArgsConstructor;
import org.cfuv.medcard.api.repository.AnalysisRepository;
import org.cfuv.medcard.api.service.AnalysisService;
import org.cfuv.medcard.api.service.AnalysisTemplateService;
import org.cfuv.medcard.api.service.CardUserService;
import org.cfuv.medcard.api.service.ImageService;
import org.cfuv.medcard.dto.AnalysisRequest;
import org.cfuv.medcard.dto.ImageFileDTO;
import org.cfuv.medcard.dto.ShareDTO;
import org.cfuv.medcard.dto.filter.AnalysisFilter;
import org.cfuv.medcard.exception.IncompatibleParametersException;
import org.cfuv.medcard.exception.ObjectNotFoundException;
import org.cfuv.medcard.model.Analysis;
import org.cfuv.medcard.model.AnalysisTemplate;
import org.cfuv.medcard.model.parameter.AnalysisParameter;
import org.cfuv.medcard.model.parameter.TemplateParameter;
import org.cfuv.medcard.model.user.CardUser;
import org.cfuv.medcard.util.filter.DxUtil;
import org.cfuv.medcard.util.filter.RequestFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AnalysisServiceImpl implements AnalysisService {


    private final AnalysisRepository analysisRepository;
    private final CardUserService cardUserService;
    private final AnalysisTemplateService analysisTemplateService;
    private final ImageService imageService;

    @Override
    public Page<Analysis> getAll(String userEmail, AnalysisFilter filter, Pageable p) {
        List<RequestFilter> filters = DxUtil.mapStringToFilters(filter.filter());
        CardUser cardUser = cardUserService.loadByEmail(userEmail);
        return filters.isEmpty() ? analysisRepository.findAllByCardUserAndDeletedIsFalse(cardUser, p)
                : analysisRepository.findAllByCardUserAndFilters(cardUser, filters, p);
    }

    @Override
    public Page<Analysis> getAll(String userEmail, ShareDTO filter, Pageable p) {
        CardUser user = cardUserService.loadByEmail(userEmail);
        return analysisRepository.findAllNotDeletedByCardUserAndTemplatesAndDate(user, filter.analysisDatesSince(), filter.ids(), p);
    }

    @Override
    public Analysis create(String userEmail, AnalysisRequest request) {
        // TODO: refactor on AnalysisMapper.INSTANCE.toEntity
        Analysis analysis = new Analysis();
        analysis.setCardUser(cardUserService.loadByEmail(userEmail));
        analysis.setName(request.name());
        analysis.setTemplate(analysisTemplateService.loadByNameAndCardUser(request.templateName(), userEmail));
        analysis.setAnalysisDate(request.analysisDate());
        if (!"custom".equals(request.templateName())) {
            analysis.setParameters(createAnalysisParametersByTemplate(userEmail, request.templateName()));
        } else {
            analysis.setParameters(new ArrayList<>());
        }
        analysis.setCommentary(request.commentary());
        return analysisRepository.save(analysis);
    }

    @Override
    public Analysis update(String userEmail, AnalysisRequest request, Long id) {
        Analysis analysis = loadByIdAndCardUser(id, userEmail);
        analysis.setName(request.name());
        analysis.setTemplate(analysisTemplateService.loadByNameAndCardUser(request.templateName(), userEmail));
        analysis.setAnalysisDate(request.analysisDate());
        if (!"custom".equals(request.templateName())) {
            checkParametersEquality(userEmail, request.templateName(), request.parameters());
        }
        analysis.setParameters(request.parameters());
        analysis.setCommentary(request.commentary());
        return analysisRepository.save(analysis);
    }

    private List<AnalysisParameter> createAnalysisParametersByTemplate(String userEmail, String analysisType) {
        AnalysisTemplate template = analysisTemplateService.loadByNameAndCardUser(analysisType, userEmail);
        List<TemplateParameter> templateParameters = template.getParameters();
        List<AnalysisParameter> analysisParameters = new ArrayList<>();
        for (TemplateParameter templateParameter : templateParameters) {
            AnalysisParameter analysisParameter = new AnalysisParameter(templateParameter.name(), null, templateParameter.unit(), null);
            analysisParameters.add(analysisParameter);
        }
        return analysisParameters;
    }

    private void checkParametersEquality(String userEmail, String analysisType, List<AnalysisParameter> parameters) {
        AnalysisTemplate templateParameter = analysisTemplateService.loadByNameAndCardUser(analysisType, userEmail);
        List<TemplateParameter> templateParameters = templateParameter.getParameters();
        if (parameters.size() != templateParameters.size()) {
            throw IncompatibleParametersException.build(analysisType);
        }
    }

    @Override
    public Analysis delete(String userEmail, Long id) {
        Analysis analysis = loadByIdAndCardUser(id, userEmail);
        analysis.setDeleted(true);
        return analysisRepository.save(analysis);
    }

    @Override
    public Analysis loadByIdAndCardUser(long id, String userEmail) {
        return analysisRepository.findByIdAndCardUserAndDeletedIsFalse(id, cardUserService.loadByEmail(userEmail))
                .orElseThrow(ObjectNotFoundException.supply(Analysis.class, id));
    }

    @Transactional
    @Override
    public String addImage(String userEmail, long id, ImageFileDTO imageFileDTO) {
        Analysis analysis = loadByIdAndCardUser(id, userEmail);
        String fileName = imageService.upload(imageFileDTO);
        List<String> images = analysis.getImages() == null ? new ArrayList<>() : analysis.getImages();
        images.add(fileName);
        analysis.setImages(images);
        analysisRepository.save(analysis);
        return fileName;
    }

    @Override
    public byte[] getImage(String userEmail, long id, String fileName) {
        return imageService.get(fileName);
    }

    @Transactional
    @Override
    public void deleteImage(String userEmail, long id, String fileName) {
        Analysis analysis = loadByIdAndCardUser(id, userEmail);
        List<String> images = analysis.getImages();
        images.remove(fileName);
        analysis.setImages(images);
        imageService.delete(fileName);
        analysisRepository.save(analysis);
    }

}
