package com.evox.evoxbackend.controller;

import com.evox.evoxbackend.dto.LoginDto;
import com.evox.evoxbackend.dto.TokenDto;
import com.evox.evoxbackend.model.User;
import com.evox.evoxbackend.security.service.AuthServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServices authServices;
    @PostMapping(path = "/login")
    public ResponseEntity<Mono<TokenDto>> login(@RequestBody LoginDto user){
        Mono<TokenDto>  token = authServices.login(user);
        return ResponseEntity.ok(token);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Mono<String>> register(@RequestBody User user) {
        Mono<String> registeredUser = authServices.accountRegistration(user);
        return ResponseEntity.ok(registeredUser);
    }






}
