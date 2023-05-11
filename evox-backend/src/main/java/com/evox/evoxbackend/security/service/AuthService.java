package com.evox.evoxbackend.security.service;


import com.evox.evoxbackend.dto.LoginDto;
import com.evox.evoxbackend.dto.TokenDto;
import com.evox.evoxbackend.exception.CustomException;
import com.evox.evoxbackend.model.User;
import com.evox.evoxbackend.model.enums.Role;
import com.evox.evoxbackend.security.jwt.JwtProvider;
import com.evox.evoxbackend.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;

    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

//    public Mono<TokenDto> login(LoginDto dto) {
//        return Mono.just(repository.findByUsernameOrEmail(dto.getEmail() ,dto.getEmail()))
//                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST,"user is not registered")))
//                .filter(user -> passwordEncoder.matches(dto.getPassword(), user.getPassword()))
//                .flatMap(user ->{
//                    if (Boolean.TRUE.equals(user.getStatus())) {
//                       return Mono.just(new TokenDto(jwtProvider.generateToken(user)));
//                    }
//                        return Mono.error(new CustomException(HttpStatus.NOT_ACCEPTABLE ,"inactive user"));
//                })
//                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "bad credentials")));
//    }

//    public Mono<TokenDto> login(LoginDto dto) {
//        return Mono.just(repository.findByUsernameOrEmail(dto.getEmail(), dto.getEmail()))
//                .filter(user -> passwordEncoder.matches(dto.getPassword(), user.getPassword()))
//                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Bad credentials")))
//                .flatMap(user -> {
//                    if (Boolean.TRUE.equals(user.getStatus())) {
//                        String token = jwtProvider.generateToken(user);
//                        return Mono.just(new TokenDto(token));
//                    } else {
//                        return Mono.error(new CustomException(HttpStatus.NOT_ACCEPTABLE, "Inactive user"));
//                    }
//                })
//                .onErrorMap(ex -> new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing the request."));
//    }


    public Mono<TokenDto> login(LoginDto dto) {
        return Mono.just(repository.findByUsernameOrEmail(dto.getEmail(), dto.getEmail()))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "No existe")))
                .map(ele -> new TokenDto("melo"));

    }


    public Mono<String> accountRegistration(User user) {
        user.setRefLink("http//:evox/ref/" + user.getUsername());
        user.setRoles(Role.ROLE_USER.name());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return Flux.fromIterable(repository.findAll())
                .any(existingUser -> existingUser.getCountry().equals(user.getCountry()) && existingUser.getIdentification().equals(user.getIdentification()))
                .flatMap(existingUser -> {
                    if (Boolean.TRUE.equals(existingUser)) {
                        return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "The user is already registered in the system"));
                    } else {
                        return multilevel(user);
                    }
                });
    }

    public Mono<String> multilevel(User user) {
        if (user.getInvitationLink() == null) {
            return Mono.just(repository.save(user)).flatMap(ele -> Mono.just("Welcome to evox " + ele.getFullName()));
        }
        return Mono.just(repository.findByUsernameIgnoreCase(extractUsername(user.getInvitationLink())))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Reference link does not exist")))
                .flatMap(parent -> {
                    user.setParent(parent);
                    parent.setId(parent.getId());
                    parent.getChildren().add(user);
                    return Mono.just(user);
                })
                .flatMap(userWithRelations -> Mono.just(repository.save(userWithRelations)))
                .flatMap(ele -> Mono.just("Welcome to evox " + ele.getFullName()));
    }

    public Mono<String> passwordRecovery(LoginDto email) {
        return Mono.just(repository.findByEmail(email.getEmail()))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "This e-mail is not registered in the database.")))
                .flatMap(ele -> {
                    if (ele.getToken() != null) {
                        return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Password recovery already in progress."));
                    }
                    ele.setStatus(false);
                    ele.setToken(UUID.randomUUID().toString());
                    ele.setId(ele.getId());
                    return Mono.just(repository.save(ele));
                }).flatMap(ele -> Mono.just("We send an email with the option to update your password."));
    }

    public Mono<Boolean> tokenValidation(String token) {
        return Flux.fromIterable(repository.findAll())
                .filter(ele -> ele.getToken().equalsIgnoreCase(token))
                .next()
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Password recovery already in progress.")))
                .then(Mono.just(false));

    }

    private String extractUsername(String refLink) {
        int position = refLink.lastIndexOf('/');
        return refLink.substring(position + 1);
    }
}
