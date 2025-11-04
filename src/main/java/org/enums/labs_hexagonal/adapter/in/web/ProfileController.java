package org.enums.labs_hexagonal.adapter.in.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.enums.labs_hexagonal.adapter.in.dto.request.ProfileRequest;
import org.enums.labs_hexagonal.adapter.in.dto.response.ProfileResponse;
import org.enums.labs_hexagonal.application.port.in.ProfileUseCase;
import org.enums.labs_hexagonal.domain.exception.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileUseCase profileUseCase;

    @PostMapping("/talent")
    public ResponseEntity<ProfileResponse> createOrUpdateProfile(
            @Valid @RequestBody ProfileRequest request,
            HttpServletRequest httpRequest) {

        String userEmail = getCurrentUserEmail(httpRequest);
        // Explicit type instead of var
        org.enums.labs_hexagonal.domain.model.TalentProfile profile =
                profileUseCase.createOrUpdateProfile(userEmail, request);

        ProfileResponse response = ProfileResponse.builder()
                .email(userEmail)
                .transcript(profile.getTranscript())
                .statementOfPurpose(profile.getStatementOfPurpose())
                .completeness(profile.calculateCompleteness())
                .missingFields(profile.getMissingFields())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getProfile(HttpServletRequest httpRequest) {
        String userEmail = getCurrentUserEmail(httpRequest);
        ProfileResponse response = profileUseCase.getProfile(userEmail);
        return ResponseEntity.ok(response);
    }

    private String getCurrentUserEmail(HttpServletRequest request) {
        // In a real implementation, extract email from JWT token
        String email = request.getHeader("X-User-Email");
        if (email == null || email.trim().isEmpty()) {
            throw new UnauthorizedException();
        }
        return email;
    }
}
