package org.enums.labs_hexagonal.domain.exception;

public class UnauthorizedException extends ApplicationException {
    public UnauthorizedException() {
        super("UNAUTHORIZED", "Please sign in to access this resource.");
    }
}
