package org.cfuv.medcard.api.repository;

import org.cfuv.medcard.model.user.CardUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardUserRepository extends JpaRepository<CardUser, Long>, JpaSpecificationExecutor<CardUser> {

    Optional<CardUser> findByEmail(String email);

    boolean existsByEmail(String email);

}
