package com.helmes.cities.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationFailedException extends AuthenticationException {

    public AuthenticationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationFailedException(String message) {
        super(message);
    }
}
