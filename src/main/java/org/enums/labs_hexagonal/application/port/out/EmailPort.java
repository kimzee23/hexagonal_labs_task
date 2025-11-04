package org.enums.labs_hexagonal.application.port.out;

public interface EmailPort {
    void sendVerificationEmail(String email, String token);
}
