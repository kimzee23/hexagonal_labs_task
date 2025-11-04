package org.enums.labs_hexagonal.adapter.out.persistence;

import org.enums.labs_hexagonal.adapter.out.persistence.mapper.PersistenceMapper;
import org.enums.labs_hexagonal.adapter.out.persistence.repository.VerificationTokenJpaRepository;
import org.enums.labs_hexagonal.application.port.out.VerificationTokenPort;
import org.enums.labs_hexagonal.domain.model.VerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VerificationTokenPersistenceAdapter implements VerificationTokenPort {

    private final VerificationTokenJpaRepository tokenRepository;
    private final PersistenceMapper mapper;

    @Override
    public VerificationToken save(VerificationToken token) {
        var entity = mapper.toVerificationTokenEntity(token);
        var savedEntity = tokenRepository.save(entity);
        return mapper.toVerificationToken(savedEntity);
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return tokenRepository.findByToken(token)
                .map(mapper::toVerificationToken);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        tokenRepository.deleteByUserId(userId);
    }
}