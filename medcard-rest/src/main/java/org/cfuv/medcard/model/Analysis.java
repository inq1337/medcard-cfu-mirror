package org.cfuv.medcard.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.cfuv.medcard.model.converter.AnalysisParameterListConverter;
import org.cfuv.medcard.model.parameter.AnalysisParameter;
import org.cfuv.medcard.model.user.CardUser;
import org.hibernate.annotations.Type;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "analysis")
public class Analysis extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String name;

    @ManyToOne(targetEntity = AnalysisTemplate.class)
    @JoinColumn(nullable = false)
    private AnalysisTemplate template;

    @Column
    private LocalDate analysisDate;

    @ManyToOne(targetEntity = CardUser.class)
    @JoinColumn(nullable = false)
    private CardUser cardUser;

    @Convert(converter = AnalysisParameterListConverter.class)
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<AnalysisParameter> parameters;

    @Column(columnDefinition = "text")
    private String commentary;

    @Column(nullable = false)
    private boolean deleted = false;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> images;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Analysis analysis = (Analysis) o;
        return getId() != null && Objects.equals(getId(), analysis.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
