package com.evox.evoxbackend.services;

import com.evox.evoxbackend.dto.SessionDto;
import com.evox.evoxbackend.dto.UserDto;
import com.evox.evoxbackend.exception.LocalNotFoundException;
import com.evox.evoxbackend.mapper.SessionMapper;
import com.evox.evoxbackend.mapper.UserMapper;
import com.evox.evoxbackend.models.Session;
import com.evox.evoxbackend.models.User;
import com.evox.evoxbackend.repository.SessionRepository;
import com.evox.evoxbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService  {
    private final UserRepository repository;
    private final SessionRepository sessionRepository;


    public Mono<User> accountRegistration(User user) {
        return Flux.fromIterable(repository.findAll())
                .any(existingUser -> existingUser.getCountry().equals(user.getCountry()) && existingUser.getIdentification().equals(user.getIdentification()))
                .flatMap(existingUser -> {
                    if (Boolean.TRUE.equals(existingUser)) {
                        return Mono.error(new LocalNotFoundException("The user is already registered in the system"));
                    } else {
                       return unilevel(user).flatMap(ele->{
                            UserDto userDto = UserMapper.userToUserDTO(ele);
                            return Mono.just(UserMapper.userDtoUser(repository.save(userDto)));
                        });
                    }
                });
    }

    public Mono<User> unilevel(User user) {
        if (!user.getRefLink().isEmpty()) {
           return Flux.fromIterable(repository.findAll())
                    .filter(ele -> ele.getRefLink()
                            .equalsIgnoreCase(user.getRefLink()))
                    .next()
                    .switchIfEmpty(Mono.error(new LocalNotFoundException("Reference link does not exist")))
                    .flatMap(parent -> {
                        user.setParent(UserMapper.userDtoUser(parent));
                        parent.getListChildren().add(UserMapper.userToUserDTO(user));
                        return Mono.just(UserMapper.userDtoUser(repository.save(parent)));
                    });
        } else {
            return Mono.just(user);
        }
    }

    public Mono<Boolean> validateAccount(String token) {
        return Flux.fromIterable(repository.findAll())
                .filter(ele->ele.getToken().equals(token))
                .next()
                .flatMap(ele->{
                    ele.setId(ele.getId());
                    ele.setEmailVerified(true);
                    ele.setToken("");
                    return Mono.just(UserMapper.userDtoUser(repository.save(ele))).map(User::getEmailVerified);
                });
    }

    public Mono<Session> startedSection(Session session){
        SessionDto sessionDto = SessionMapper.sessionToSessionDto(session);
        return Mono.just(SessionMapper.sessionDtoSession(sessionRepository.save(sessionDto)));
    }




}
