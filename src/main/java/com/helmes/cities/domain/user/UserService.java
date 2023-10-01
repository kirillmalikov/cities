package com.helmes.cities.domain.user;

import com.helmes.cities.auth.JwtService;
import com.helmes.cities.domain.role.repository.RoleRepository;
import com.helmes.cities.domain.user.api.v1.model.CreateUserRequest;
import com.helmes.cities.domain.user.entity.User;
import com.helmes.cities.domain.user.repository.UserRepository;
import com.helmes.cities.exception.AuthenticationFailedException;
import com.helmes.cities.exception.ConflictException;
import com.helmes.cities.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.helmes.cities.domain.role.Roles.ROLE_ALLOW_VIEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    public User create(CreateUserRequest request) throws ConflictException {
        var user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        if (request.isAdmin()) {
            user.getRoles().addAll(roleRepository.findAll());
        } else {
            roleRepository.findByName(ROLE_ALLOW_VIEW.name()).map(it -> user.getRoles().add(it));
        }

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("email", String.format("User with email %s already exists", user.getEmail()));
        }
    }

    public String authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            log.warn("Invalid credentials {}: {}", email, password);

            throw new AuthenticationFailedException("Invalid credentials", e);
        }

        return jwtService.generateToken(userDetailsService.loadUserByUsername(email));
    }

    public User getCurrent() {
        var jwt = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        Optional<User> user = Optional.empty();

        if (!jwt.isBlank()) {
            var email = jwtService.extractUsername(jwt);
            user = userRepository.findByEmail(email);
        }

        return user.orElseThrow(() -> new NotFoundException("User", "No user was found by provided JWT token"));
    }
}
