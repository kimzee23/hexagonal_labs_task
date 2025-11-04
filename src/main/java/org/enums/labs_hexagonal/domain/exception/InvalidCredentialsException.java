package org.enums.labs_hexagonal.domain.exception;

public class InvalidCredentialsException extends ApplicationException {
    public InvalidCredentialsException() {
        super("INVALID_CREDENTIALS", "The email or password you entered is incorrect. Please try again or reset your password.");
    }
}
