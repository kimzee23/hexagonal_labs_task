package org.enums.labs_hexagonal.domain.exception;

public class InvalidTokenException extends ApplicationException {
    public InvalidTokenException() {
        super("TOKEN_INVALID", "This verification link is invalid. Please request a new verification link.");
    }
}