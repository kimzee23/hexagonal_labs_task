package org.enums.labs_hexagonal.adapter.out.persistence.repository;

import org.enums.labs_hexagonal.adapter.out.persistence.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionJpaRepository extends JpaRepository<SessionEntity, UUID> {
    Optional<SessionEntity> findByToken(String token);

    @Query("SELECT COUNT(s) > 0 FROM SessionEntity s WHERE s.userId = :userId AND s.revoked = false AND s.expiresAt > CURRENT_TIMESTAMP")
    boolean existsValidSessionByUserId(@Param("userId") UUID userId);
}