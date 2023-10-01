package com.helmes.cities.domain.user.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helmes.cities.domain.role.entity.Role;
import com.helmes.cities.domain.user.api.v1.model.CreateUserRequest;
import com.helmes.cities.domain.user.repository.UserRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * {@link UserController}
 */

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerCreateUserIntegrationTest {

    private static final String URI = "/v1/user";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void withInvalidEmailShouldReturnBadRequest() throws Exception {
        var request = new CreateUserRequest(
                "invalid.email.com",
                "Qwerty123",
                false
        );

        mockMvc.perform(composePostRequest(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.rows", hasSize(1)))
                .andExpect(jsonPath("$.type", is("ARGUMENT_NOT_VALID")))
                .andExpect(jsonPath("$.rows[0].field", is("email")))
                .andExpect(jsonPath("$.rows[0].reason", is("Email")))
                .andExpect(jsonPath("$.rows[0].message", is("must be a well-formed email address")));
    }

    @Test
    void withBlankFieldsShouldReturnBadRequest() throws Exception {
        var request = new CreateUserRequest(
                "",
                "",
                false
        );

        mockMvc.perform(composePostRequest(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.rows", hasSize(2)))
                .andExpect(jsonPath("$.type", is("ARGUMENT_NOT_VALID")))
                .andExpect(jsonPath("$.rows[0].field", is("email")))
                .andExpect(jsonPath("$.rows[0].reason", is("NotBlank")))
                .andExpect(jsonPath("$.rows[0].message", is("must not be blank")))
                .andExpect(jsonPath("$.rows[1].field", is("password")))
                .andExpect(jsonPath("$.rows[1].reason", is("NotBlank")))
                .andExpect(jsonPath("$.rows[1].message", is("must not be blank")));
    }

    @Test
    void withValidInputShouldCreateUserWithReadRole() throws Exception {
        var request = new CreateUserRequest(
                "test@test.email.com",
                "Qwerty123",
                false
        );

        var response = mockMvc.perform(composePostRequest(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.roles", hasSize(1)))
                .andExpect(jsonPath("$.roles[0]", aMapWithSize(2)))
                .andExpect(jsonPath("$.roles[0].id", is(notNullValue())))
                .andExpect(jsonPath("$.roles[0].name", is("ROLE_ALLOW_VIEW")))
                .andReturn();

        var userId = Long.valueOf(JsonPath.read(response.getResponse().getContentAsString(), "$.id").toString());

        var userOptional = userRepository.findById(userId);

        assertThat(userOptional.isPresent()).isTrue();
        var user = userOptional.get();
        assertThat(user.getEmail()).isEqualTo("test@test.email.com");
        assertThat(user.getPassword()).isNotEqualTo("Qwerty123");
        assertThat(passwordEncoder.matches("Qwerty123", user.getPassword())).isTrue();
        assertThat(user.getRoles().size()).isEqualTo(1);
        assertThat(user.getRoles().stream().map(Role::getName).anyMatch("ROLE_ALLOW_VIEW"::equals)).isTrue();
    }

    @Test
    void withValidInputAndAdminShouldCreateUserWithAllRoles() throws Exception {
        var request = new CreateUserRequest(
                "test@test.email.com",
                "Qwerty123",
                true
        );

        var response = mockMvc.perform(composePostRequest(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.roles", hasSize(2)))
                .andReturn();

        var userId = Long.valueOf(JsonPath.read(response.getResponse().getContentAsString(), "$.id").toString());

        var userOptional = userRepository.findById(userId);

        assertThat(userOptional.isPresent()).isTrue();
        var user = userOptional.get();
        assertThat(user.getEmail()).isEqualTo("test@test.email.com");
        assertThat(user.getPassword()).isNotEqualTo("Qwerty123");
        assertThat(passwordEncoder.matches("Qwerty123", user.getPassword())).isTrue();
        assertThat(user.getRoles().size()).isEqualTo(2);
        assertThat(user.getRoles()).extracting(Role::getName).containsOnly("ROLE_ALLOW_VIEW", "ROLE_ALLOW_EDIT");
    }

    @Test
    @Transactional
    void withEmailAlreadyExistingCreateUserShouldReturnConflict() throws Exception {
        var request = new CreateUserRequest(
                "test@test.email.com",
                "Qwerty123",
                false
        );

        mockMvc.perform(composePostRequest(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(composePostRequest(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.field", is("email")))
                .andExpect(jsonPath("$.message", is("User with email test@test.email.com already exists")));
    }

    private MockHttpServletRequestBuilder composePostRequest(String content) {
        return post(URI).content(content).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
    }
}
