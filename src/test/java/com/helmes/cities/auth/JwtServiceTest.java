package com.helmes.cities.auth;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtServiceTest {

    @Mock
    private UserDetails userDetails;

    private final JwtService jwtService = new JwtService();

    @BeforeAll
    void beforeAll() {
        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", "secret");
    }

    @Test
    void testGenerateToken() {
        assertThat(jwtService.generateToken(userDetails)).isNotEmpty();
    }

    @Test
    void testValidateTokenValidUser() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);

        assertThat(jwtService.validateToken(token, userDetails)).isTrue();
    }

    @Test
    void testValidateTokenInvalidUser() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.generateToken(userDetails);
        when(userDetails.getUsername()).thenReturn("anotherUser");

        assertThat(jwtService.validateToken(token, userDetails)).isFalse();
    }

    @Test
    void testExtractUsername() {
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtService.generateToken(userDetails);
        String extractedUsername = jwtService.extractUsername(token);

        assertThat(extractedUsername).isEqualTo("testUser");
    }
}
