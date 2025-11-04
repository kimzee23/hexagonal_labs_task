package org.enums.labs_hexagonal.application.port.in;


import org.enums.labs_hexagonal.adapter.in.dto.request.AuthDto;
import org.enums.labs_hexagonal.adapter.in.dto.request.LoginRequest;
import org.enums.labs_hexagonal.adapter.in.dto.request.SignupRequest;
import org.enums.labs_hexagonal.adapter.in.dto.response.AuthResponse;

public interface AuthUseCase {
    AuthResponse signup(SignupRequest request);
    void verifyEmail(String token);
    AuthResponse login(LoginRequest request);
    void logout(String sessionToken);
}

