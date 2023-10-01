package com.helmes.cities.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotFoundException extends CitiesException {

    private String entity;
    private String message;
}
