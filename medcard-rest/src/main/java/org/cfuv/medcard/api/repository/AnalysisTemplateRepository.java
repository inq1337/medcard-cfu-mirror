package org.cfuv.medcard.api.repository;

import org.cfuv.medcard.dto.TemplateSimpleItem;
import org.cfuv.medcard.model.AnalysisTemplate;
import org.cfuv.medcard.model.user.CardUser;
import org.cfuv.medcard.util.filter.RequestFilter;
import org.cfuv.medcard.util.specification.AnalysisTemplateSpecificationFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface AnalysisTemplateRepository extends JpaRepository<AnalysisTemplate, Long>, JpaSpecificationExecutor<AnalysisTemplate> {

    default Page<AnalysisTemplate> findAllByCardUserAndFilters(CardUser cardUser,
                                                               List<RequestFilter> filters,
                                                               Pageable pageable) {
        Specification<AnalysisTemplate> sf = new AnalysisTemplateSpecificationFilter(filters)
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cardUser"), cardUser))
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), false));
        return findAll(sf, pageable);
    }

    List<TemplateSimpleItem> findAllByCardUserAndDeletedIsFalse(CardUser cardUser);

    Page<AnalysisTemplate> findAllByCardUserAndDeletedIsFalse(CardUser user, Pageable pageable);

    Optional<AnalysisTemplate> findByIdAndCardUserAndDeletedIsFalse(Long id, CardUser user);

    int countByCardUserAndNameAndDeletedIsFalse(CardUser cardUser, String name);

    int countByCardUserAndDeletedIsFalse(CardUser cardUser);
}
