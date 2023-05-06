package com.evox.evoxbackend.controller;

import com.evox.evoxbackend.models.User;
import com.evox.evoxbackend.services.AuthService;
import com.evox.evoxbackend.utils.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(path = "/authenticate")
    public ResponseEntity<UserResponse> authenticate(){
       return null;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Mono<User>> register(@RequestBody User user) {
        System.out.println("entra");
        Mono<User> registeredUser = authService.accountRegistration(user);
        return ResponseEntity.ok(registeredUser);
    }
}
