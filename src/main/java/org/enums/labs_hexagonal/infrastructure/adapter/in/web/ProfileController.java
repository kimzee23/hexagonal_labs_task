package org.enums.labs_hexagonal.infrastructure.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.enums.labs_hexagonal.domain.model.TalentProfile;
import org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.request.ProfileRequest;
import org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.response.ProfileResponse;
import org.enums.labs_hexagonal.application.port.in.ProfileUseCase;
import org.enums.labs_hexagonal.application.port.out.SessionPort;
import org.enums.labs_hexagonal.application.port.out.UserRepositoryPort;
import org.enums.labs_hexagonal.domain.exception.UnauthorizedException;
import org.enums.labs_hexagonal.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.enums.labs_hexagonal.domain.model.Session;

@Slf4j
@RestController
@RequestMapping("/v1/profile")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "Talent profile management APIs")
public class ProfileController {

    private final ProfileUseCase profileUseCase;
    private final SessionPort sessionPort;
    private final UserRepositoryPort userRepository;

    @Operation(
            summary = "Create or update talent profile",
            description = "Create or update talent profile with transcript and statement of purpose",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile saved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/talent")
    public ResponseEntity<?> createOrUpdateProfile(
            @Valid @RequestBody ProfileRequest request,
            HttpServletRequest httpRequest) {

        String userEmail = getCurrentUserEmailFromToken(httpRequest);
        TalentProfile profile = profileUseCase.createOrUpdateProfile(userEmail, request);

        return ResponseEntity.ok(ProfileResponse.builder()
                .email(userEmail)
                .transcript(profile.getTranscript())
                .statementOfPurpose(profile.getStatementOfPurpose())
                .completeness(profile.calculateCompleteness())
                .missingFields(profile.getMissingFields())
                .build());
    }

    @Operation(
            summary = "Get my profile",
            description = "Get current user's profile with completeness information",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getProfile(HttpServletRequest httpRequest) {
        String userEmail = getCurrentUserEmailFromToken(httpRequest);
        var response = profileUseCase.getProfile(userEmail);
        return ResponseEntity.ok(response);
    }

    private String getCurrentUserEmailFromToken(HttpServletRequest request) {
        String token = extractToken(request);
        if (token == null) {
            throw new UnauthorizedException();
        }

        Session session = sessionPort.findByToken(token)
                .orElseThrow(UnauthorizedException::new);

        if (!session.isValid()) {
            throw new UnauthorizedException();
        }

        User user = userRepository.findById(session.getUserId())
                .orElseThrow(UnauthorizedException::new);

        return user.getEmail();
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}