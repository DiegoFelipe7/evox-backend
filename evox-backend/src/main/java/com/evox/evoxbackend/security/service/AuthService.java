package com.evox.evoxbackend.security.service;


import com.evox.evoxbackend.dto.LoginDto;
import com.evox.evoxbackend.dto.TokenDto;
import com.evox.evoxbackend.exception.CustomException;
import com.evox.evoxbackend.model.Session;
import com.evox.evoxbackend.model.User;
import com.evox.evoxbackend.model.enums.Role;
import com.evox.evoxbackend.utils.enums.TypeStateResponse;
import com.evox.evoxbackend.repository.SessionRepository;
import com.evox.evoxbackend.security.jwt.JwtProvider;
import com.evox.evoxbackend.security.repository.AuthRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final SessionRepository sessionRepository;
    private final ModelMapper mapper;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public Mono<TokenDto> login(LoginDto dto) {
        return authRepository.findByEmailIgnoreCase(dto.getEmail())
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "user is not registered" , TypeStateResponse.Error)))
                .filter(user -> passwordEncoder.matches(dto.getPassword(), user.getPassword()))
                .flatMap(user -> {
                    if (Boolean.TRUE.equals(user.getStatus())) {
                        return entryRegister(dto).map(ele->new TokenDto(jwtProvider.generateToken(user)));
                    }
                    return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "inactive user" , TypeStateResponse.Warning));
                })
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "bad credentials" , TypeStateResponse.Error)));
    }

    public Mono<Session> entryRegister(LoginDto dto) {
       return authRepository.findByEmailIgnoreCase(dto.getEmail())
                .flatMap(ele -> {
                    Session session = new Session(ele.getId(), dto.getIpAddress(), dto.getCountry(), dto.getBrowser(), LocalDateTime.now());
                    return sessionRepository.save(session);
                });
    }

    public Mono<String> accountRegistration(User user) {
        user.setRefLink("https://evox/ref/" + user.getUsername());
        user.setRoles(Role.ROLE_USER.name());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setCreatedAt(LocalDateTime.now());
        return authRepository.findAll()
                .any(existingUser -> existingUser.getCountry().equals(user.getCountry()) && existingUser.getIdentification().equals(user.getIdentification()))
                .flatMap(existingUser -> {
                    if (Boolean.TRUE.equals(existingUser)) {
                        return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "The user is already registered in the system" , TypeStateResponse.Error));
                    }
                        return multilevel(user);
                });
    }

    public Mono<String> multilevel(User user) {
        if (user.getInvitationLink() == null) {
            return authRepository.save(user).flatMap(ele -> Mono.just("Welcome to evox " + ele.getFullName()));
        }
        return authRepository.findByUsernameIgnoreCase(extractUsername(user.getInvitationLink()))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Reference link does not exist" , TypeStateResponse.Warning)))
                .flatMap(parent -> {
                    user.setParentId(parent.getParentId());
                    return authRepository.save(user);
                })
                .flatMap(ele -> Mono.just("Welcome to evox " + ele.getFullName()));

    }

    public Mono<String> passwordRecovery(LoginDto email) {
        return authRepository.findByEmailIgnoreCase(email.getEmail())
                .filter(User::getStatus)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "You do not have a verified account" , TypeStateResponse.Warning)))
                .flatMap(ele -> {
                    ele.setId(ele.getId());
                    ele.setToken(UUID.randomUUID().toString());
                    return authRepository.save(ele);
                })
                .flatMap(updatedUser -> Mono.just("We sent an email with the option to update your password."));
    }

    public Mono<Boolean> tokenValidation(String token) {
        return authRepository.findByToken(token)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "invalid token",TypeStateResponse.Error)))
                .filter(ele -> ele.getToken().equals(token))
                .hasElement()
                .defaultIfEmpty(false);

    }

    public Mono<String> passwordChange(String token, LoginDto dto) {
        return authRepository.findByToken(token)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Sorry this token is invalid" ,TypeStateResponse.Error)))
                .flatMap(ele -> {
                    String encodedPassword = passwordEncoder.encode(dto.getPassword());
                    ele.setId(ele.getId());
                    ele.setPassword(encodedPassword);
                    ele.setToken("");
                    return authRepository.save(ele);
                }).flatMap(ele -> Mono.just("password successfully updated"));
    }

    private String extractUsername(String refLink) {
        int position = refLink.lastIndexOf('/');
        return refLink.substring(position + 1);
    }
}
