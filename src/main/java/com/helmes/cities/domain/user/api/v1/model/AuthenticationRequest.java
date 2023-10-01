package com.helmes.cities.domain.user.api.v1.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record AuthenticationRequest(
        @Email
        @NotBlank
        String email,

        @NotBlank
        String password
) {}
