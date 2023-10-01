package com.helmes.cities.domain.user.api.v1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.http.Cookie;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * {@link UserController}
 */

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerLogoutUserIntegrationTest {

    private static final String URI = "/v1/user/logout";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void withAnyJwtCookieShouldSetItBlankAndReturnOk() throws Exception {
        mockMvc.perform(composeGetRequest().cookie(new Cookie("jwt", UUID.randomUUID().toString())))
                .andExpect(status().isOk())
                .andExpect(cookie().value("jwt", ""));
    }

    private MockHttpServletRequestBuilder composeGetRequest() {
        return post(URI).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
    }
}
