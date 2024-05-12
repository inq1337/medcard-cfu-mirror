package org.cfuv.medcard.api.service;

import org.cfuv.medcard.dto.AnalysisRequest;
import org.cfuv.medcard.dto.ImageFileDTO;
import org.cfuv.medcard.dto.ShareDTO;
import org.cfuv.medcard.dto.filter.AnalysisFilter;
import org.cfuv.medcard.model.Analysis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnalysisService {

    Page<Analysis> getAll(String userEmail, AnalysisFilter filter, Pageable p);

    Page<Analysis> getAll(String userEmail, ShareDTO filter, Pageable p);

    Analysis create(String userEmail, AnalysisRequest request);

    Analysis update(String userEmail, AnalysisRequest request, Long id);

    Analysis delete(String userEmail, Long id);

    Analysis loadByIdAndCardUser(long id, String userEmail);

    String addImage(String userEmail, long id, ImageFileDTO imageFileDTO);

    byte[] getImage(String userEmail, long id, String fileName);

    void deleteImage(String userEmail, long id, String fileName);
}
