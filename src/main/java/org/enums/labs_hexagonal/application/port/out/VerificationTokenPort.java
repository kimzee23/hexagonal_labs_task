package org.enums.labs_hexagonal.application.port.out;

import org.enums.labs_hexagonal.domain.model.VerificationToken;

import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenPort {
    VerificationToken save(VerificationToken token);

    Optional<VerificationToken> findByToken(String token);

    void deleteByUserId(UUID userId);
}
