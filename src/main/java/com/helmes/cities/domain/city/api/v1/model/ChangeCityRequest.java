package com.helmes.cities.domain.city.api.v1.model;

import javax.validation.constraints.NotBlank;

public record ChangeCityRequest(
   @NotBlank
   String name,
   @NotBlank
   String photo
) {}
