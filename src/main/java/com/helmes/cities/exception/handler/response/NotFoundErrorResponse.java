package com.helmes.cities.exception.handler.response;

import com.helmes.cities.exception.NotFoundException;
import lombok.Getter;

@Getter
public class NotFoundErrorResponse {

    private final String entity;
    private final String message;

    public NotFoundErrorResponse(NotFoundException e) {
        this.entity = e.getEntity();
        this.message = e.getMessage();
    }
}
