package com.helmes.cities.exception.handler.response;

import com.helmes.cities.exception.ConflictException;
import lombok.Getter;

@Getter
public class ConflictErrorResponse {

    private final String field;
    private final String message;

    public ConflictErrorResponse(ConflictException e) {
        this.field = e.getField();
        this.message = e.getMessage();
    }
}
