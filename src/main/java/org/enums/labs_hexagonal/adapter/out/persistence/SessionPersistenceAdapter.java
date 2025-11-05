package org.enums.labs_hexagonal.adapter.out.persistence;

import org.enums.labs_hexagonal.adapter.out.persistence.mapper.PersistenceMapper;
import org.enums.labs_hexagonal.adapter.out.persistence.repository.SessionJpaRepository;
import org.enums.labs_hexagonal.application.port.out.SessionPort;
import org.enums.labs_hexagonal.domain.model.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SessionPersistenceAdapter implements SessionPort {

    private final SessionJpaRepository sessionRepository;
    private final PersistenceMapper mapper;

    @Override
    public Session save(Session session) {
        var entity = mapper.toSessionEntity(session);
        var savedEntity = sessionRepository.save(entity);
        return mapper.toSession(savedEntity);
    }

    @Override
    public Optional<Session> findByToken(String token) {
        return sessionRepository.findByToken(token)
                .map(mapper::toSession);
    }

    @Override
    public void revokeSession(String token) {
        sessionRepository.findByToken(token).ifPresent(sessionEntity -> {
            sessionEntity.setRevoked(true);
            sessionRepository.save(sessionEntity);
        });
    }

    @Override
    public boolean existsValidSession(UUID userId) {
        return sessionRepository.existsValidSessionByUserId(userId);
    }
}