package org.enums.labs_hexagonal;

import org.enums.labs_hexagonal.adapter.in.dto.request.ProfileRequest;
import org.enums.labs_hexagonal.adapter.in.dto.response.ProfileResponse;
import org.enums.labs_hexagonal.application.port.out.TalentProfilePort;
import org.enums.labs_hexagonal.application.port.out.UserRepositoryPort;
import org.enums.labs_hexagonal.application.service.ProfileService;
import org.enums.labs_hexagonal.domain.model.TalentProfile;
import org.enums.labs_hexagonal.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private TalentProfilePort talentProfilePort;

    @Mock
    private UserRepositoryPort userRepository;

    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        profileService = new ProfileService(talentProfilePort, userRepository);
    }

    @Test
    void createOrUpdateProfile_NewProfile_Success() {
        String userEmail = "ade@gmail.com";
        ProfileRequest request = ProfileRequest.builder()
                .transcript("Transcript content")
                .statementOfPurpose("SOP content")
                .build();

        User user = User.builder()
                .id(UUID.randomUUID())
                .email(userEmail)
                .build();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(talentProfilePort.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(talentProfilePort.save(any(TalentProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TalentProfile profile = profileService.createOrUpdateProfile(userEmail, request);

        assertNotNull(profile);
        assertEquals("Transcript content", profile.getTranscript());
        assertEquals("SOP content", profile.getStatementOfPurpose());
        assertEquals(100, profile.calculateCompleteness());
    }

    @Test
    void getProfile_ExistingProfile_ReturnsCompleteInfo() {

        String userEmail = "ade@gmail.com";
        User user = User.builder()
                .id(UUID.randomUUID())
                .email(userEmail)
                .build();

        TalentProfile talentProfile = TalentProfile.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .transcript("Transcript content")
                .statementOfPurpose("SOP content")
                .build();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(talentProfilePort.findByUserId(user.getId())).thenReturn(Optional.of(talentProfile));

        ProfileResponse response = profileService.getProfile(userEmail);

        assertEquals(userEmail, response.getEmail());
        assertEquals(100, response.getCompleteness());
        assertTrue(response.getMissingFields().isEmpty());
    }
}
