package com.helmes.cities.auth;

import com.helmes.cities.domain.role.entity.Role;
import com.helmes.cities.domain.user.repository.UserRepository;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(username)
                .map(user -> new org.springframework.security.core.userdetails.User(user.getEmail(),
                                                                                    user.getPassword(),
                                                                                    getGrantedAuthorities(user.getRoles())
                )).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    private List<SimpleGrantedAuthority> getGrantedAuthorities(Collection<Role> roles) {
        return roles.stream().map(it -> new SimpleGrantedAuthority(it.getName())).toList();
    }
}
