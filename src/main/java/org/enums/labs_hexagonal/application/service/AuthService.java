package org.enums.labs_hexagonal.application.service;

import lombok.RequiredArgsConstructor;
import org.enums.labs_hexagonal.adapter.in.dto.request.LoginRequest;
import org.enums.labs_hexagonal.adapter.in.dto.request.SignupRequest;
import org.enums.labs_hexagonal.adapter.in.dto.response.AuthResponse;
import org.enums.labs_hexagonal.application.port.in.AuthUseCase;
import org.enums.labs_hexagonal.application.port.out.EmailPort;
import org.enums.labs_hexagonal.application.port.out.SessionPort;
import org.enums.labs_hexagonal.application.port.out.UserRepositoryPort;
import org.enums.labs_hexagonal.application.port.out.VerificationTokenPort;
import org.enums.labs_hexagonal.domain.exception.*;
import org.enums.labs_hexagonal.domain.model.Session;
import org.enums.labs_hexagonal.domain.model.User;
import org.enums.labs_hexagonal.domain.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final UserRepositoryPort userRepository;
    private final VerificationTokenPort verificationTokenPort;
    private final SessionPort sessionPort;
    private final EmailPort emailPort;
    private final PasswordEncoder passwordEncoder;

    private static final int TOKEN_EXPIRY_HOURS = 24;
    private static final int SESSION_EXPIRY_DAYS = 7;

    @Override
    public AuthResponse signup(SignupRequest request) {
        Optional<User> existingUserOptional = userRepository.findByEmail(request.getEmail());

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            if (existingUser.isVerified()) {
                throw new EmailAlreadyExistsException();
            } else {

                verificationTokenPort.deleteByUserId(existingUser.getId());
                VerificationToken token = createVerificationToken(existingUser.getId());
                emailPort.sendVerificationEmail(existingUser.getEmail(), token.getToken());
                return AuthResponse.builder()
                        .message("VERIFICATION_RESENT")
                        .email(existingUser.getEmail())
                        .build();
            }
        }

        User user = User.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(User.UserStatus.PENDING_VERIFICATION)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        VerificationToken token = createVerificationToken(savedUser.getId());
        emailPort.sendVerificationEmail(savedUser.getEmail(), token.getToken());

        return AuthResponse.builder()
                .message("PENDING_VERIFICATION")
                .email(savedUser.getEmail())
                .build();
    }

    @Override
    public void verifyEmail(String tokenValue) {
        VerificationToken token = verificationTokenPort.findByToken(tokenValue)
                .orElseThrow(InvalidTokenException::new);

        if (token.isUsed()) {
            throw new TokenAlreadyUsedException();
        }

        if (token.isExpired()) {
            throw new TokenExpiredException();
        }

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(InvalidTokenException::new);

        user.setStatus(User.UserStatus.VERIFIED);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        token.markAsUsed();
        verificationTokenPort.save(token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!user.isVerified()) {
            throw new EmailNotVerifiedException();
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        Session session = Session.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusDays(SESSION_EXPIRY_DAYS))
                .createdAt(LocalDateTime.now())
                .revoked(false)
                .build();

        Session savedSession = sessionPort.save(session);

        return AuthResponse.builder()
                .message("LOGIN_SUCCESS")
                .email(user.getEmail())
                .sessionToken(savedSession.getToken())
                .build();
    }

    @Override
    public void logout(String sessionToken) {
        sessionPort.revokeSession(sessionToken);
    }

    private VerificationToken createVerificationToken(UUID userId) {
        VerificationToken token = VerificationToken.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusHours(TOKEN_EXPIRY_HOURS))
                .createdAt(LocalDateTime.now())
                .build();
        return verificationTokenPort.save(token);
    }
}
