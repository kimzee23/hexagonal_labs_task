package org.enums.labs_hexagonal.application.port.out;

import org.enums.labs_hexagonal.domain.model.Session;

import java.util.Optional;
import java.util.UUID;

public interface SessionPort {
    Session save(Session session);
    Optional<Session> findByToken(String token);
    void revokeSession(String token);
    boolean existsValidSession(UUID userId);
}