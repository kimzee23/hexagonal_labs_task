package org.enums.labs_hexagonal.application.port.in;

import org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.request.ProfileRequest;
import org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.response.ProfileResponse;
import org.enums.labs_hexagonal.domain.model.TalentProfile;

public interface ProfileUseCase {
    TalentProfile createOrUpdateProfile(String userEmail, ProfileRequest request);

    ProfileResponse getProfile(String userEmail);
}
