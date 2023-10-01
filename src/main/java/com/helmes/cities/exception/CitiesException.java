package com.helmes.cities.exception;

public class CitiesException extends RuntimeException {

    protected CitiesException() {
        super();
    }

    public CitiesException(String message) {
        super(message);
    }

    public CitiesException(String message, Throwable cause) {
        super(message, cause);
    }
}
