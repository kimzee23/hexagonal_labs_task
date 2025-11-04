package org.enums.labs_hexagonal.application.port.out;

import org.enums.labs_hexagonal.domain.model.TalentProfile;

import java.util.Optional;
import java.util.UUID;

public interface TalentProfilePort {
    TalentProfile save(TalentProfile profile);

    Optional<TalentProfile> findByUserId(UUID userId);
}
