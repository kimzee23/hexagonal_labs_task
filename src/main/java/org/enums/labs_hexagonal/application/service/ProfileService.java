package org.enums.labs_hexagonal.application.service;

import lombok.RequiredArgsConstructor;
import org.enums.labs_hexagonal.adapter.in.dto.request.ProfileRequest;
import org.enums.labs_hexagonal.adapter.in.dto.response.ProfileResponse;
import org.enums.labs_hexagonal.application.port.in.ProfileUseCase;
import org.enums.labs_hexagonal.application.port.out.TalentProfilePort;
import org.enums.labs_hexagonal.application.port.out.UserRepositoryPort;
import org.enums.labs_hexagonal.domain.model.TalentProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.enums.labs_hexagonal.domain.exception.UnauthorizedException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileService implements ProfileUseCase {

    private final TalentProfilePort talentProfilePort;
    private final UserRepositoryPort userRepository;

    @Override
    public TalentProfile createOrUpdateProfile(String userEmail, ProfileRequest request) {
        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(UnauthorizedException::new);

        var existingProfile = talentProfilePort.findByUserId(user.getId());

        if (existingProfile.isPresent()) {
            var profile = existingProfile.get();
            profile.setTranscript(request.getTranscript());
            profile.setStatementOfPurpose(request.getStatementOfPurpose());
            profile.setUpdatedAt(LocalDateTime.now());
            return talentProfilePort.save(profile);
        } else {
            var profile = TalentProfile.builder()
                    .id(UUID.randomUUID())
                    .userId(user.getId())
                    .transcript(request.getTranscript())
                    .statementOfPurpose(request.getStatementOfPurpose())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            return talentProfilePort.save(profile);
        }
    }

    @Override
    public ProfileResponse getProfile(String userEmail) {
        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(UnauthorizedException::new);

        var profile = talentProfilePort.findByUserId(user.getId());

        if (profile.isEmpty()) {
            return ProfileResponse.builder()
                    .email(user.getEmail())
                    .completeness(0)
                    .missingFields(java.util.List.of("transcript", "statementOfPurpose"))
                    .build();
        }

        var talentProfile = profile.get();
        return ProfileResponse.builder()
                .email(user.getEmail())
                .transcript(talentProfile.getTranscript())
                .statementOfPurpose(talentProfile.getStatementOfPurpose())
                .completeness(talentProfile.calculateCompleteness())
                .missingFields(talentProfile.getMissingFields())
                .build();
    }
}
