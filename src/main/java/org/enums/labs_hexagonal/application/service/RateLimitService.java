package org.enums.labs_hexagonal.application.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.enums.labs_hexagonal.domain.exception.RateLimitException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitService {

    private static final int MAX_LOGIN_ATTEMPTS = 6;
    private static final int LOCKOUT_DURATION_MINUTES = 2;

    private final Map<String, LoginAttempt> loginAttempts = new ConcurrentHashMap<>();

    public void checkRateLimit(String email) {
        String key = "login:" + email;
        LoginAttempt attempt = loginAttempts.get(key);

        if (attempt != null && attempt.isLocked()) {
            log.warn("Rate limit exceeded for email: {}", email);
            throw new RateLimitException();
        }
    }

    public void recordLoginAttempt(String email, boolean successful) {
        String key = "login:" + email;
        LoginAttempt attempt = loginAttempts.get(key);

        if (attempt == null) {
            attempt = new LoginAttempt();
            loginAttempts.put(key, attempt);
        }

        if (successful) {
            attempt.reset();
        } else {
            attempt.recordFailure();
        }
        cleanupExpiredEntries();
    }

    private void cleanupExpiredEntries() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(LOCKOUT_DURATION_MINUTES);
        loginAttempts.entrySet().removeIf(entry ->
                entry.getValue().getLastAttempt().isBefore(cutoff) &&
                        entry.getValue().getAttemptCount() == 0
        );
    }

    @Data
    private static class LoginAttempt {
        private int attemptCount = 0;
        private LocalDateTime lastAttempt = LocalDateTime.now();
        private LocalDateTime lockedUntil;

        public void recordFailure() {
            this.attemptCount++;
            this.lastAttempt = LocalDateTime.now();

            if (this.attemptCount >= MAX_LOGIN_ATTEMPTS) {
                this.lockedUntil = LocalDateTime.now().plusMinutes(LOCKOUT_DURATION_MINUTES);
            }
        }

        public void reset() {
            this.attemptCount = 0;
            this.lockedUntil = null;
            this.lastAttempt = LocalDateTime.now();
        }

        public boolean isLocked() {
            return lockedUntil != null && LocalDateTime.now().isBefore(lockedUntil);
        }
    }
}