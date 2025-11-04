package org.enums.labs_hexagonal.adapter.out.persistence.repository;

import org.enums.labs_hexagonal.adapter.out.persistence.entity.SessionEntity;
import org.enums.labs_hexagonal.adapter.out.persistence.entity.TalentProfileEntity;
import org.enums.labs_hexagonal.adapter.out.persistence.entity.UserEntity;
import org.enums.labs_hexagonal.adapter.out.persistence.entity.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}





