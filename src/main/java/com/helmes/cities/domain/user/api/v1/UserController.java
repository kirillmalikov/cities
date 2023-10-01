package com.helmes.cities.domain.user.api.v1;

import static com.helmes.cities.domain.user.UserMapper.toResponse;
import static org.springframework.http.HttpStatus.*;

import com.helmes.cities.domain.user.UserService;
import com.helmes.cities.domain.user.api.v1.model.AuthenticationRequest;
import com.helmes.cities.domain.user.api.v1.model.AuthenticationResponse;
import com.helmes.cities.domain.user.api.v1.model.CreateUserRequest;
import com.helmes.cities.domain.user.api.v1.model.UserResponse;
import com.helmes.cities.domain.user.repository.UserRepository;
import com.helmes.cities.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private static final String JWT_COOKIE_VALUE = "jwt=%s; HttpOnly; Path=/";

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@Validated @RequestBody CreateUserRequest request) throws ConflictException {
        return new ResponseEntity<>(toResponse(userService.create(request)), CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Void> authenticate(@Validated @RequestBody AuthenticationRequest request) {
        var jwt = userService.authenticate(request.email(), request.password());
        var headers = new HttpHeaders();

        headers.add(HttpHeaders.SET_COOKIE, String.format(JWT_COOKIE_VALUE, jwt));

        return ResponseEntity.ok().headers(headers).body(null);
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUser() {
        return new ResponseEntity<>(toResponse(userService.getCurrent()), OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logOut() {
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, String.format(JWT_COOKIE_VALUE, ""));

        return ResponseEntity.ok().headers(headers).body(null);
    }
}
