package com.evox.evoxbackend.services;

import com.evox.evoxbackend.dto.MultiLevelDto;
import com.evox.evoxbackend.dto.UserDto;
import com.evox.evoxbackend.exception.CustomException;
import com.evox.evoxbackend.repository.UserRepository;

import com.evox.evoxbackend.security.jwt.JwtProvider;
import com.evox.evoxbackend.utils.enums.TypeStateResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class UserServices {
    private final UserRepository repository;
    private final ModelMapper modelMapper;
    private final JwtProvider jwtProvider;


    public Flux<MultiLevelDto> getAllMultilevel(String token) {
        var userName = jwtProvider.extractToken(token);
        return repository.findUserAndDescendants(userName)
                .flatMap(data -> {
                    if (data.getParentId() == null) {
                        return Mono.just(new MultiLevelDto(data.getRefLink(), data.getUsername(), data.getFullName(), data.getStatus(), data.getCreatedAt()));
                    }
                    return repository.findById(data.getParentId())
                            .map(ele -> new MultiLevelDto(ele.getFullName(),data.getRefLink(), data.getUsername(), data.getFullName(), data.getStatus(), data.getCreatedAt()));
                })
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "An error has occurred, please contact the administrator", TypeStateResponse.Error)));

    }

}
