package com.helmes.cities.domain.user.api.v1;

import com.helmes.cities.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * {@link UserController}
 */

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerGetUserIntegrationTest {

    private static final String URI = "/v1/user";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtils testUtils;

    @Test
    void withAbsentJwtCookieShouldReturnForbidden() throws Exception {
        mockMvc.perform(composeGetRequest())
                .andExpect(status().isForbidden());
    }

    @Test
    void withInvalidJwtCookieShouldReturnForbidden() throws Exception {
        mockMvc.perform(composeGetRequest().cookie(new Cookie("jwt", "")))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void withValidJwtCookieShouldReturnValidResponse() throws Exception {
        var user = testUtils.saveUser("test@test.email.com", "Qwerty123", "ROLE_TEST");

        mockMvc.perform(composeGetRequest().cookie(new Cookie("jwt", testUtils.generateJwtForUser(user))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.roles", hasSize(1)))
                .andExpect(jsonPath("$.roles[0].name", is("ROLE_TEST")));
    }

    private MockHttpServletRequestBuilder composeGetRequest() {
        return get(URI).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
    }
}
