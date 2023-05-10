package com.evox.evoxbackend.security.handler;

import com.evox.evoxbackend.dto.LoginDto;
import com.evox.evoxbackend.dto.TokenDto;
import com.evox.evoxbackend.model.User;
import com.evox.evoxbackend.security.service.AuthServices;
import com.evox.evoxbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthHandler {
    private final AuthServices authServices;
    public Mono<ServerResponse> login(ServerRequest request) {
        Mono<LoginDto> dtoMono = request.bodyToMono(LoginDto.class);
        return dtoMono
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(authServices.login(dto), TokenDto.class));
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<User> dtoMono = request.bodyToMono(User.class);
        return dtoMono
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(authServices.accountRegistration(dto), User.class));
    }
}
