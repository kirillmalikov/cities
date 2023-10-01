package com.helmes.cities.auth;

import com.helmes.cities.domain.role.entity.Role;
import com.helmes.cities.domain.user.entity.User;
import com.helmes.cities.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;


    @Test
    void testLoadUserByUsername() {
        var email = "test@test.email.com";
        var password = "Qwerty123";
        var role = new Role("TEST_ROLE");
        var user = new User();

        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(role));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        var userDetails = userDetailsService.loadUserByUsername(email);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isEqualTo(password);
        assertThat((Set<SimpleGrantedAuthority>) userDetails.getAuthorities())
                .contains(new SimpleGrantedAuthority(role.getName()));
    }

    @Test
    void testLoadUserByUsernameUserNotFound() {
        String username = "nonExistentUser";

        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> userDetailsService.loadUserByUsername(username));
    }
}
