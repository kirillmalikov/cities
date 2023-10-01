package com.helmes.cities.auth;

import java.io.IOException;
import java.util.stream.Stream;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final var cookies = request.getCookies();

        if (cookies != null) {
            var authCookie = Stream.of(cookies).filter(it -> it.getName().equals("jwt")).findFirst();

            String username;
            String jwt;

            jwt = authCookie.map(Cookie::getValue).orElse("");

            if (!jwt.isBlank()) {
                try {
                    username = jwtService.extractUsername(jwt);

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        var userDetails = userDetailsService.loadUserByUsername(username);

                        if (jwtService.validateToken(jwt, userDetails)) {
                            var authToken = new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    jwt,
                                    userDetails.getAuthorities()
                            );

                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        }
                    }
                } catch (JwtException | UsernameNotFoundException ignored) {}
            }
        }

        filterChain.doFilter(request, response);
    }
}
