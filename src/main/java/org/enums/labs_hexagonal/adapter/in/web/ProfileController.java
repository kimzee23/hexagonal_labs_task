package org.enums.labs_hexagonal.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.enums.labs_hexagonal.adapter.in.dto.request.ProfileRequest;
import org.enums.labs_hexagonal.adapter.in.dto.response.ProfileResponse;
import org.enums.labs_hexagonal.application.port.in.ProfileUseCase;
import org.enums.labs_hexagonal.domain.exception.UnauthorizedException;
import org.enums.labs_hexagonal.domain.model.TalentProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/profile")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "Talent profile management APIs")
public class ProfileController {

    private final ProfileUseCase profileUseCase;

    @PostMapping("/talent")
    @Operation(
            summary = "Create or update talent profile",
            description = "Create or update talent profile with transcript and statement of purpose",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile saved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ProfileResponse> createOrUpdateProfile(
            @Valid @RequestBody ProfileRequest request,
            HttpServletRequest httpRequest) {

        String userEmail = getCurrentUserEmail(httpRequest);
        TalentProfile profileResponse = profileUseCase.createOrUpdateProfile(userEmail, request);

        return ResponseEntity.ok(ProfileResponse.builder()
                .email(userEmail)
                .transcript(profileResponse.getTranscript())
                .statementOfPurpose(profileResponse.getStatementOfPurpose())
                .completeness(profileResponse.calculateCompleteness())
                .missingFields(profileResponse.getMissingFields())
                .build());
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get my profile",
            description = "Get current user's profile with completeness information",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ProfileResponse> getProfile(HttpServletRequest httpRequest) {
        String userEmail = getCurrentUserEmail(httpRequest);
        ProfileResponse profileResponse = profileUseCase.getProfile(userEmail);
        return ResponseEntity.ok(profileResponse);
    }

    private String getCurrentUserEmail(HttpServletRequest request) {
        String email = request.getHeader("X-User-Email");
        if (email == null || email.trim().isEmpty()) {
            throw new UnauthorizedException();
        }
        return email;
    }
}
