package com.helmes.cities.domain.city.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helmes.cities.TestUtils;
import com.helmes.cities.domain.city.api.v1.model.ChangeCityRequest;
import com.helmes.cities.domain.city.entity.City;
import com.helmes.cities.domain.city.repository.CityRepository;
import com.helmes.cities.domain.role.Roles;
import com.helmes.cities.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * {@link CitiesController}
 */

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CitiesControllerChangeCityIntegrationTest {

    private static final String URI = "/v1/cities/";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestUtils testUtils;
    @Autowired
    private CityRepository cityRepository;

    @Test
    void withoutJwtCookieShouldReturnForbidden() throws Exception {
        mockMvc.perform(composePutRequest(1L, "{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void withInvalidJwtCookieShouldReturnForbidden() throws Exception {
        mockMvc.perform(composePutRequest(1L, "{}").cookie(new Cookie("jwt", "invalid")))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void withBlankRequestInputShouldReturnBadRequest() throws Exception {
        var user = creadeAdminUser();
        var request = new ChangeCityRequest("", "");

        mockMvc.perform(composePutRequest(1L, objectMapper.writeValueAsString(request))
                        .cookie(new Cookie("jwt", testUtils.generateJwtForUser(user))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.rows", hasSize(2)))
                .andExpect(jsonPath("$.type", is("ARGUMENT_NOT_VALID")))
                .andExpect(jsonPath("$.rows[0].field", is("name")))
                .andExpect(jsonPath("$.rows[0].reason", is("NotBlank")))
                .andExpect(jsonPath("$.rows[0].message", is("must not be blank")))
                .andExpect(jsonPath("$.rows[1].field", is("photo")))
                .andExpect(jsonPath("$.rows[1].reason", is("NotBlank")))
                .andExpect(jsonPath("$.rows[1].message", is("must not be blank")));
    }

    @Test
    @Transactional
    void withValidJwtCookieAndPartialNameShouldReturnValidResponseIgnoringCase() throws Exception {
        var user = creadeAdminUser();
        var request = new ChangeCityRequest("TestName", "TestPhoto");

        mockMvc.perform(composePutRequest(1L, objectMapper.writeValueAsString(request))
                        .cookie(new Cookie("jwt", testUtils.generateJwtForUser(user))))
                .andExpect(status().isNoContent());

        var city = cityRepository.findById(1L);
        assertThat(city.isPresent()).isTrue();
        assertThat(city.get()).extracting(City::getName, City::getPhoto).containsOnly("TestName", "TestPhoto");
    }

    private MockHttpServletRequestBuilder composePutRequest(Long id, String body) {
        return put(URI + id)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    private User creadeAdminUser() {
        return testUtils.saveUser("test@test.email.com", "Qwerty123", Roles.ROLE_ALLOW_EDIT.name());
    }
}
