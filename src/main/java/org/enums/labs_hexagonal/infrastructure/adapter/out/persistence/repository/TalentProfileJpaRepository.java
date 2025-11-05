package org.enums.labs_hexagonal.infrastructure.adapter.out.persistence.repository;

import org.enums.labs_hexagonal.infrastructure.adapter.out.persistence.entity.TalentProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface  TalentProfileJpaRepository extends JpaRepository<TalentProfileEntity, UUID> {
    Optional<TalentProfileEntity> findByUserId(UUID userId);
}
