package org.cfuv.medcard.api.repository;

import org.cfuv.medcard.model.Analysis;
import org.cfuv.medcard.model.user.CardUser;
import org.cfuv.medcard.util.filter.RequestFilter;
import org.cfuv.medcard.util.specification.AnalysisSpecificationFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AnalysisRepository extends JpaRepository<Analysis, Long>, JpaSpecificationExecutor<Analysis> {

    default Page<Analysis> findAllByCardUserAndFilters(CardUser cardUser,
                                                       List<RequestFilter> filters,
                                                       Pageable pageable) {
        Specification<Analysis> sf = new AnalysisSpecificationFilter(filters)
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cardUser"), cardUser))
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleted"), false));
        return findAll(sf, pageable);
    }

    Page<Analysis> findAllByCardUserAndDeletedIsFalse(CardUser user, Pageable pageable);

    Optional<Analysis> findByIdAndCardUserAndDeletedIsFalse(Long id, CardUser user);

    @Query("""
            select a from Analysis a
            where a.cardUser = ?1 and a.analysisDate >= ?2 and a.template.id in ?3 and a.deleted = false""")
    Page<Analysis> findAllNotDeletedByCardUserAndTemplatesAndDate(CardUser cardUser,
                                                                  LocalDate analysisDate,
                                                                  Collection<Long> templateId,
                                                                  Pageable p);

}
