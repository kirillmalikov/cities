package com.helmes.cities.domain.user;

import com.helmes.cities.domain.user.api.v1.model.UserResponse;
import com.helmes.cities.domain.user.entity.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getRoles());
    }
}
