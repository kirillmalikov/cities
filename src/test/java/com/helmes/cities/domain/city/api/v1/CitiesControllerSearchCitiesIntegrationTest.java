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
class CitiesControllerSearchCitiesIntegrationTest {

    private static final String URI = "/v1/cities/search";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtils testUtils;

    @Test
    void withoutJwtCookieShouldReturnForbidden() throws Exception {
        mockMvc.perform(composeGetRequest("Tallinn", 0, 10))
                .andExpect(status().isForbidden());
    }

    @Test
    void withInvalidJwtCookieShouldReturnForbidden() throws Exception {
        mockMvc.perform(composeGetRequest("Tallinn", 0, 10).cookie(new Cookie("jwt", "invalid")))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void withValidJwtCookieAndExactNameShouldReturnValidResponseWithSingleResult() throws Exception {
        var user = testUtils.saveUser("test@test.email.com", "Qwerty123", Roles.ROLE_ALLOW_VIEW.name());
        var name = "Tallinn";
        var page = 0;
        var size = 10;

        mockMvc.perform(composeGetRequest(name, page, size).cookie(new Cookie("jwt", testUtils.generateJwtForUser(user))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(11)))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0]", aMapWithSize(3)))
                .andExpect(jsonPath("$.content[0].id", is(notNullValue())))
                .andExpect(jsonPath("$.content[0].name", is(name)))
                .andExpect(jsonPath("$.content[0].photo", is(notNullValue())))
                .andExpect(jsonPath("$.pageable", aMapWithSize(6)))
                .andExpect(jsonPath("$.last", is(true)))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.size", is(size)))
                .andExpect(jsonPath("$.number", is(page)))
                .andExpect(jsonPath("$.sort", aMapWithSize(3)))
                .andExpect(jsonPath("$.first", is(true)))
                .andExpect(jsonPath("$.numberOfElements", is(1)))
                .andExpect(jsonPath("$.empty", is(false)));
    }

    @Test
    @Transactional
    void withValidJwtCookieAndPartialNameShouldReturnValidResponseIgnoringCase() throws Exception {
        var user = testUtils.saveUser("test@test.email.com", "Qwerty123", Roles.ROLE_ALLOW_VIEW.name());
        var name = "aLl";
        var page = 0;
        var size = 10;

        mockMvc.perform(composeGetRequest(name, page, size).cookie(new Cookie("jwt", testUtils.generateJwtForUser(user))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(11)))
                .andExpect(jsonPath("$.content", hasSize(6)))
                .andExpect(jsonPath("$.content[0].name", containsStringIgnoringCase(name)))
                .andExpect(jsonPath("$.content[1].name", containsStringIgnoringCase(name)))
                .andExpect(jsonPath("$.content[2].name", containsStringIgnoringCase(name)))
                .andExpect(jsonPath("$.content[3].name", containsStringIgnoringCase(name)))
                .andExpect(jsonPath("$.content[4].name", containsStringIgnoringCase(name)))
                .andExpect(jsonPath("$.content[5].name", containsStringIgnoringCase(name)))
                .andExpect(jsonPath("$.pageable", aMapWithSize(6)))
                .andExpect(jsonPath("$.last", is(true)))
                .andExpect(jsonPath("$.totalElements", is(6)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.size", is(10)))
                .andExpect(jsonPath("$.number", is(page)))
                .andExpect(jsonPath("$.sort", aMapWithSize(3)))
                .andExpect(jsonPath("$.first", is(true)))
                .andExpect(jsonPath("$.numberOfElements", is(6)))
                .andExpect(jsonPath("$.empty", is(false)));
    }

    private MockHttpServletRequestBuilder composeGetRequest(String name, int page, int size) {
        return get(URI + String.format("?name=%s&page=%d&size=%d", name, page, size))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }
}