package org.enums.labs_hexagonal.domain.exception;

public class TokenExpiredException extends ApplicationException {
    public TokenExpiredException() {
        super("TOKEN_EXPIRED", "This verification link has expired. Please request a new verification link.");
    }
}