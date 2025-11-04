package org.enums.labs_hexagonal.domain.exception;

public class EmailAlreadyExistsException extends ApplicationException {
    public EmailAlreadyExistsException() {
        super("EMAIL_IN_USE", "This email is already registered. Please use a different email or recover your account.");
    }
}