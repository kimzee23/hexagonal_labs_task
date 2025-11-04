package org.enums.labs_hexagonal.domain.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final String errorCode;
    private final String message;

    public ApplicationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
}















