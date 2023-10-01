package com.helmes.cities.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConflictException extends Throwable {
    private String field;
    private String message;
}
