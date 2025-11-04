package org.enums.labs_hexagonal.domain.exception;

public class TokenAlreadyUsedException extends ApplicationException {
    public TokenAlreadyUsedException() {
        super("TOKEN_ALREADY_USED", "This verification link has already been used. Your account is already verified and you can log in.");
    }
}