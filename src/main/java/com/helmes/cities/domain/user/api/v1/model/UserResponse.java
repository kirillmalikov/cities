package com.helmes.cities.domain.user.api.v1.model;

import com.helmes.cities.domain.role.entity.Role;
import java.util.Set;

public record UserResponse(
        Long id,
        Set<Role> roles
) {}
