package org.enums.labs_hexagonal.domain.exception;

public class RateLimitException extends ApplicationException {
    public RateLimitException() {
        super("RATE_LIMITED", "Too many login attempts. Please wait a few minutes and try again.");
    }
}