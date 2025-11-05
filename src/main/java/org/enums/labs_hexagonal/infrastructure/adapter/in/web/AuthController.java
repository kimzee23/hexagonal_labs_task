package org.enums.labs_hexagonal.infrastructure.adapter.in.web;


import org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.request.LoginRequest;
import org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.request.SignupRequest;
import org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.request.VerifyEmailRequest;
import org.enums.labs_hexagonal.infrastructure.adapter.in.web.dto.response.AuthResponse;
import org.enums.labs_hexagonal.application.port.in.AuthUseCase;
import org.enums.labs_hexagonal.domain.exception.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication APIs for talent users")
public class AuthController {

    private final AuthUseCase authUseCase;

    @PostMapping("/signup")
    @Operation(summary = "User signup", description = "Create a new talent user account")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or email already exists")
    })
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authUseCase.signup(request));
    }

    @Operation(summary = "Verify email", description = "Verify user email with token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email verified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid, expired or already used token")
    })
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        authUseCase.verifyEmail(request.getToken());
        return ResponseEntity.ok().body(java.util.Map.of(
                "message", "Email verified successfully. You can now log in."
        ));
    }


    @PostMapping("/login")
    @Operation(summary = "User login", description = "Login with email and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials or email not verified")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authUseCase.login(request));
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logout and revoke current session")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Logout successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null) authUseCase.logout(token);
        return ResponseEntity.noContent().build();
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer "))
                ? bearerToken.substring(7)
                : null;
    }

    @ExceptionHandler({
            EmailAlreadyExistsException.class,
            EmailNotVerifiedException.class,
            InvalidCredentialsException.class,
            TokenExpiredException.class,
            TokenAlreadyUsedException.class,
            InvalidTokenException.class,
            RateLimitException.class
    })
    public ResponseEntity<Map<String, String>> handleApplicationExceptions(ApplicationException ex) {
        return ResponseEntity.status(getHttpStatus(ex))
                .body(Map.of(
                        "error", ex.getErrorCode(),
                        "message", ex.getMessage()
                ));
    }

    private HttpStatus getHttpStatus(ApplicationException ex) {
        return switch (ex.getErrorCode()) {
            case "EMAIL_IN_USE", "EMAIL_NOT_VERIFIED", "INVALID_CREDENTIALS",
                 "TOKEN_EXPIRED", "TOKEN_ALREADY_USED", "TOKEN_INVALID" -> HttpStatus.BAD_REQUEST;
            case "RATE_LIMITED" -> HttpStatus.TOO_MANY_REQUESTS;
            case "UNAUTHORIZED" -> HttpStatus.UNAUTHORIZED;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
