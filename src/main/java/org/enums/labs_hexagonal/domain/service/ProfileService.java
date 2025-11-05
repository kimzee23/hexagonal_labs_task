package org.enums.labs_hexagonal.domain.service;

import lombok.RequiredArgsConstructor;
import org.enums.labs_hexagonal.domain.model.User;
import org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.request.ProfileRequest;
import org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.response.ProfileResponse;
import org.enums.labs_hexagonal.application.port.in.ProfileUseCase;
import org.enums.labs_hexagonal.application.port.out.TalentProfilePort;
import org.enums.labs_hexagonal.application.port.out.UserRepositoryPort;
import org.enums.labs_hexagonal.domain.model.TalentProfile;
import org.enums.labs_hexagonal.domain.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService implements ProfileUseCase {

    private final TalentProfilePort talentProfilePort;
    private final UserRepositoryPort userRepository;

    @Override
    public TalentProfile createOrUpdateProfile(String userEmail, ProfileRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(UnauthorizedException::new);

        Optional<TalentProfile> existingProfile = talentProfilePort.findByUserId(user.getId());

        if (existingProfile.isPresent()) {
            TalentProfile profile = existingProfile.get();
            profile.setTranscript(request.getTranscript());
            profile.setStatementOfPurpose(request.getStatementOfPurpose());
            profile.setUpdatedAt(LocalDateTime.now());
            return talentProfilePort.save(profile);
        }

        TalentProfile newProfile = TalentProfile.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .transcript(request.getTranscript())
                .statementOfPurpose(request.getStatementOfPurpose())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return talentProfilePort.save(newProfile);
    }

    @Override
    public ProfileResponse getProfile(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(UnauthorizedException::new);

        Optional<TalentProfile> profileOptional = talentProfilePort.findByUserId(user.getId());

        if (profileOptional.isEmpty()) {
            return ProfileResponse.builder()
                    .email(user.getEmail())
                    .completeness(0)
                    .missingFields(List.of("transcript", "statementOfPurpose"))
                    .build();
        }

        TalentProfile talentProfile = profileOptional.get();

        return ProfileResponse.builder()
                .email(user.getEmail())
                .transcript(talentProfile.getTranscript())
                .statementOfPurpose(talentProfile.getStatementOfPurpose())
                .completeness(talentProfile.calculateCompleteness())
                .missingFields(talentProfile.getMissingFields())
                .build();
    }
}
