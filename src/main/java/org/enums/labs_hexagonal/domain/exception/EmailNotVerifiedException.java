package org.enums.labs_hexagonal.domain.exception;

public class EmailNotVerifiedException extends ApplicationException {
    public EmailNotVerifiedException() {
        super("EMAIL_NOT_VERIFIED", "Please verify your email before logging in. Check your inbox or request a new verification link.");
    }
}
