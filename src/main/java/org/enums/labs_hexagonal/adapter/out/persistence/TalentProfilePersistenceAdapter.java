package org.enums.labs_hexagonal.adapter.out.persistence;

import org.enums.labs_hexagonal.adapter.out.persistence.mapper.PersistenceMapper;
import org.enums.labs_hexagonal.adapter.out.persistence.repository.TalentProfileJpaRepository;
import org.enums.labs_hexagonal.application.port.out.TalentProfilePort;
import org.enums.labs_hexagonal.domain.model.TalentProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TalentProfilePersistenceAdapter implements TalentProfilePort {

    private final TalentProfileJpaRepository profileRepository;
    private final PersistenceMapper mapper;

    @Override
    public TalentProfile save(TalentProfile profile) {
        var entity = mapper.toTalentProfileEntity(profile);
        var savedEntity = profileRepository.save(entity);
        return mapper.toTalentProfile(savedEntity);
    }

    @Override
    public Optional<TalentProfile> findByUserId(UUID userId) {
        return profileRepository.findByUserId(userId)
                .map(mapper::toTalentProfile);
    }
}