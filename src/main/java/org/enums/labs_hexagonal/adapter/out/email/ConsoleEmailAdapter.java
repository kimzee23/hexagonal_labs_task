package org.enums.labs_hexagonal.adapter.out.email;

import lombok.extern.slf4j.Slf4j;
import org.enums.labs_hexagonal.application.port.out.EmailPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Primary
public class ConsoleEmailAdapter implements EmailPort {

    @Override
    public void sendVerificationEmail(String email, String token) {
        log.info("=== EMAIL VERIFICATION (CONSOLE) ===");
        log.info("To: {}", email);
        log.info("Token: {}", token);
        log.info("Endpoint: POST http://localhost:8081/v1/auth/verify-email");
        log.info("Body: {\"token\": \"{}\"}", token);
        log.info("=== END ===");
    }
}