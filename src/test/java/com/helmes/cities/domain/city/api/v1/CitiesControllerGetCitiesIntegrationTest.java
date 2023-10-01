package com.helmes.cities.domain.city.api.v1;

import com.helmes.cities.TestUtils;
import com.helmes.cities.domain.role.Roles;
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
 * {@link CitiesController}
 */

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CitiesControllerGetCitiesIntegrationTest {

    private static final String URI = "/v1/cities";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtils testUtils;

    @Test
    void withoutJwtCookieShouldReturnForbidden() throws Exception {
        mockMvc.perform(composeGetRequest(0, 10))
                .andExpect(status().isForbidden());
    }

    @Test
    void withInvalidJwtCookieShouldReturnForbidden() throws Exception {
        mockMvc.perform(composeGetRequest(0, 10).cookie(new Cookie("jwt", "invalid")))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void withValidJwtCookieShouldReturnValidResponse() throws Exception {
        var user = testUtils.saveUser("test@test.email.com", "Qwerty123", Roles.ROLE_ALLOW_VIEW.name());
        var page = 0;
        var size = 10;

        mockMvc.perform(composeGetRequest(page, size).cookie(new Cookie("jwt", testUtils.generateJwtForUser(user))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(11)))
                .andExpect(jsonPath("$.content", hasSize(size)))
                .andExpect(jsonPath("$.pageable", aMapWithSize(6)))
                .andExpect(jsonPath("$.last", is(false)))
                .andExpect(jsonPath("$.totalElements", is(1000)))
                .andExpect(jsonPath("$.totalPages", is(100)))
                .andExpect(jsonPath("$.size", is(size)))
                .andExpect(jsonPath("$.number", is(page)))
                .andExpect(jsonPath("$.sort", aMapWithSize(3)))
                .andExpect(jsonPath("$.first", is(true)))
                .andExpect(jsonPath("$.numberOfElements", is(size)))
                .andExpect(jsonPath("$.empty", is(false)));
    }

    private MockHttpServletRequestBuilder composeGetRequest(int page, int size) {
        return get(URI + String.format("?page=%d&size=%d", page, size))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }
}