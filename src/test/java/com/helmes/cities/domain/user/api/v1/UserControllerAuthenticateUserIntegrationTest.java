package com.helmes.cities.domain.user.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helmes.cities.TestUtils;
import com.helmes.cities.auth.JwtService;
import com.helmes.cities.domain.user.api.v1.model.AuthenticationRequest;
import com.helmes.cities.domain.user.entity.User;
import com.helmes.cities.domain.user.repository.UserRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * {@link UserController}
 */

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerAuthenticateUserIntegrationTest {

    private static final String URI = "/v1/user/authenticate";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestUtils testUtils;
    @Autowired
    private JwtService jwtService;


    @Test
    void withAbsentInputAuthenticateShouldReturnBadRequest() throws Exception {
        mockMvc.perform(composePostRequest("{}"))
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
    void withInvalidCredentialsAuthenticateShouldUnauthorized() throws Exception {
        var request = new AuthenticationRequest(
                "test@test.email.com",
                "Qwerty123"
        );

        mockMvc.perform(composePostRequest(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    void withValidCredentialsAuthenticateShouldReturnOkAndJwtCookie() throws Exception {
        var email = "test@test.email.com";
        var password = "Qwerty123";
        testUtils.saveUser(email, password);

        var request = new AuthenticationRequest(email, password);

        var response = mockMvc.perform(composePostRequest(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("jwt"))
                .andReturn();

        var cookie = response.getResponse().getCookie("jwt");
        assertThat(cookie).isNotNull();
        var jwt = cookie.getValue();
        assertThat(jwtService.extractUsername(jwt)).isEqualTo(email);
    }

    private MockHttpServletRequestBuilder composePostRequest(String content) {
        return post(URI).content(content).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
    }
}
