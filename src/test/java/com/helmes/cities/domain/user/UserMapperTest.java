package com.helmes.cities.domain.user;

import com.helmes.cities.domain.role.entity.Role;
import com.helmes.cities.domain.user.api.v1.model.UserResponse;
import com.helmes.cities.domain.user.entity.User;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    @Test
    void toResponseShouldMapUserAndReturnValidResponse() {
        var user = new User();
        user.setId(new Random().nextLong());
        user.setRoles(Set.of(new Role("TEST_ROLE")));

        var response = UserMapper.toResponse(user);

        assertThat(response)
                .extracting(UserResponse::id, UserResponse::roles)
                .containsOnly(user.getId(), user.getRoles());
    }
}
