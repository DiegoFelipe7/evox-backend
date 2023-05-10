package com.evox.evoxbackend.security.service;

import com.evox.evoxbackend.dto.LoginDto;
import com.evox.evoxbackend.dto.TokenDto;
import com.evox.evoxbackend.exception.LocalNotFoundException;
import com.evox.evoxbackend.model.User;
import com.evox.evoxbackend.model.enums.Role;
import com.evox.evoxbackend.security.jtw.JwtProvider;
import com.evox.evoxbackend.security.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthServices {
    private final AuthRepository repository;

    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    public Mono<TokenDto> login(LoginDto dto) {
        return Mono.just(repository.findByUsernameOrEmail(dto.getEmail(), dto.getEmail()))
                .filter(user -> passwordEncoder.matches(dto.getPassword(), user.getPassword()))
                .map(user -> new TokenDto(jwtProvider.generateToken(user)))
                .switchIfEmpty(Mono.error(new LocalNotFoundException("bad credentials")));
    }



    public Mono<String> accountRegistration(User user) {
        user.setRefLink("http//:evox/ref/"+user.getUsername());
        passwordEncoder.encode(user.getPassword());
        user.setRoles(Role.ROLE_USER.name());
        return Flux.fromIterable(repository.findAll())
                .any(existingUser -> existingUser.getCountry().equals(user.getCountry()) && existingUser.getIdentification().equals(user.getIdentification()))
                .flatMap(existingUser -> {
                    if (Boolean.TRUE.equals(existingUser)) {
                        return Mono.error(new LocalNotFoundException("The user is already registered in the system"));
                    } else {
                        return multilevel(user);
                    }
                });
    }

    public Mono<String> multilevel(User user) {
        if (user.getInvitationLink()==null) {
            return Mono.just(repository.save(user)).flatMap(ele->Mono.just("Welcome to evox " +ele.getFullName()));
        }
        return Mono.just(repository.findByUsernameIgnoreCase(extractUsername(user.getInvitationLink())))
                .switchIfEmpty(Mono.error(new LocalNotFoundException("Reference link does not exist")))
                .flatMap(parent -> {
                    user.setParent(parent);
                    parent.setId(parent.getId());
                    parent.getChildren().add(user);
                    return Mono.just(user);
                })
                .flatMap(userWithRelations -> Mono.just(repository.save(userWithRelations)))
                .flatMap(ele->Mono.just("Welcome to evox " +ele.getFullName()));
    }



    private String extractUsername(String refLink) {
        int position = refLink.lastIndexOf('/');
        return refLink.substring(position + 1);
    }
}
