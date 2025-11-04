package org.enums.labs_hexagonal.application.port.in;

import org.enums.labs_hexagonal.adapter.in.dto.request.ProfileRequest;
import org.enums.labs_hexagonal.adapter.in.dto.response.ProfileResponse;
import org.enums.labs_hexagonal.domain.model.TalentProfile;

public interface ProfileUseCase {
    TalentProfile createOrUpdateProfile(String userEmail, ProfileRequest request);

    ProfileResponse getProfile(String userEmail);
}
