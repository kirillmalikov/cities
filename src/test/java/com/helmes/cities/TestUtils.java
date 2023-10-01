package com.helmes.cities;

import com.helmes.cities.auth.JwtService;
import com.helmes.cities.domain.role.entity.Role;
import com.helmes.cities.domain.role.repository.RoleRepository;
import com.helmes.cities.domain.user.entity.User;
import com.helmes.cities.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TestUtils {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public User saveUser(String email, String password, String... roles) {
        var user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        for (String roleName : roles) {
            user.getRoles().add(
                    roleRepository.findByName(roleName)
                            .orElseGet(() -> roleRepository.save(new Role(roleName)))
            );
        }

        return userRepository.save(user);
    }

    public String generateJwtForUser(User user) {
        return jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        user.getRoles().stream().map(it -> new SimpleGrantedAuthority(it.getName())).toList()
                )
        );
    }
}
