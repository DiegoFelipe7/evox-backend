package com.evox.evoxbackend.security.handler;
import com.evox.evoxbackend.dto.LoginDto;
import com.evox.evoxbackend.dto.TokenDto;
import com.evox.evoxbackend.model.User;
import com.evox.evoxbackend.security.service.AuthService;
import com.evox.evoxbackend.validation.ObjectValidator;
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

    private final AuthService authService;
    private final ObjectValidator objectValidator;


    public Mono<ServerResponse> login(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(LoginDto.class)
            .flatMap(ele->ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(authService.login(ele) , TokenDto.class));

    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(User.class).doOnNext(objectValidator::validate)
                .flatMap(ele->ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(authService.accountRegistration(ele), User.class));
    }

    public Mono<ServerResponse> passwordRecovery(ServerRequest serverRequest){
        return serverRequest.bodyToMono(LoginDto.class)
                .flatMap(ele->ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(authService.passwordRecovery(ele) ,LoginDto.class));
    }

    public Mono<ServerResponse> validateToken(ServerRequest serverRequest){
        String token = serverRequest.pathVariable("token");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(authService.tokenValidation(token), Boolean.class);
    }
}
