package org.enums.labs_hexagonal.adapter.in.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.enums.labs_hexagonal.adapter.in.dto.request.LoginRequest;
import org.enums.labs_hexagonal.adapter.in.dto.request.SignupRequest;
import org.enums.labs_hexagonal.adapter.in.dto.request.VerifyEmailRequest;
import org.enums.labs_hexagonal.adapter.in.dto.response.AuthResponse;
import org.enums.labs_hexagonal.application.port.in.AuthUseCase;
import org.enums.labs_hexagonal.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        var response = authUseCase.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        authUseCase.verifyEmail(request.getToken());
        return ResponseEntity.ok().body(java.util.Map.of(
                "message", "Email verified successfully. You can now log in."
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        var response = authUseCase.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpRequest) {
        String token = extractToken(httpRequest);
        if (token != null) {
            authUseCase.logout(token);
        }
        return ResponseEntity.noContent().build();
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
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
    public ResponseEntity<Object> handleApplicationExceptions(RuntimeException ex) {
        if (ex instanceof ApplicationException appEx) {
            return ResponseEntity.status(getHttpStatus(appEx))
                    .body(java.util.Map.of(
                            "error", appEx.getErrorCode(),
                            "message", appEx.getMessage()
                    ));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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