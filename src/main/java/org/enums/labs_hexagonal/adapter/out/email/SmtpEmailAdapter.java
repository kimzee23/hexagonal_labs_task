package org.enums.labs_hexagonal.adapter.out.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.enums.labs_hexagonal.application.port.out.EmailPort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmtpEmailAdapter implements EmailPort {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String email, String token) {
        String verificationLink = "http://localhost:8080/v1/auth/verify-email?token=" + token;

        log.info("Sending verification email to: {}", email);
        log.info("Verification token: {}", token);
        log.info("Verification link: {}", verificationLink);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verify your email");
        message.setText("Hi!\n\nPlease verify your email by clicking the link below:\n" + verificationLink);
        message.setFrom("no-reply@Enum_Talent.com");

        mailSender.send(message);
    }
}
