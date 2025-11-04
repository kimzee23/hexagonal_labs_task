package org.enums.labs_hexagonal.adapter.out.persistence.mapper;

import org.enums.labs_hexagonal.adapter.out.persistence.entity.SessionEntity;
import org.enums.labs_hexagonal.adapter.out.persistence.entity.TalentProfileEntity;
import org.enums.labs_hexagonal.adapter.out.persistence.entity.UserEntity;
import org.enums.labs_hexagonal.adapter.out.persistence.entity.VerificationTokenEntity;
import org.enums.labs_hexagonal.domain.model.Session;
import org.enums.labs_hexagonal.domain.model.TalentProfile;
import org.enums.labs_hexagonal.domain.model.User;
import org.enums.labs_hexagonal.domain.model.VerificationToken;
import org.springframework.stereotype.Component;

@Component
public class PersistenceMapper {


    public UserEntity toUserEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                UserEntity.UserStatus.valueOf(user.getStatus().name()),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public User toUser(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .status(User.UserStatus.valueOf(entity.getStatus().name()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    // VerificationToken mappings
    public VerificationTokenEntity toVerificationTokenEntity(VerificationToken token) {
        return new VerificationTokenEntity(
                token.getId(),
                token.getUserId(),
                token.getToken(),
                token.getExpiresAt(),
                token.getUsedAt(),
                token.getCreatedAt()
        );
    }

    public VerificationToken toVerificationToken(VerificationTokenEntity entity) {
        return VerificationToken.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .token(entity.getToken())
                .expiresAt(entity.getExpiresAt())
                .usedAt(entity.getUsedAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    // Session mappings
    public SessionEntity toSessionEntity(Session session) {
        return new SessionEntity(
                session.getId(),
                session.getUserId(),
                session.getToken(),
                session.getExpiresAt(),
                session.getCreatedAt(),
                session.isRevoked()
        );
    }

    public Session toSession(SessionEntity entity) {
        return Session.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .token(entity.getToken())
                .expiresAt(entity.getExpiresAt())
                .createdAt(entity.getCreatedAt())
                .revoked(entity.isRevoked())
                .build();
    }
    public TalentProfileEntity toTalentProfileEntity(TalentProfile profile) {
        return new TalentProfileEntity(
                profile.getId(),
                profile.getUserId(),
                profile.getTranscript(),
                profile.getStatementOfPurpose(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }

    public TalentProfile toTalentProfile(TalentProfileEntity entity) {
        return TalentProfile.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .transcript(entity.getTranscript())
                .statementOfPurpose(entity.getStatementOfPurpose())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}