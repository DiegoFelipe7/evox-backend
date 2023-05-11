package com.evox.evoxbackend.services;

import com.evox.evoxbackend.dto.MultiLevelDto;
import com.evox.evoxbackend.dto.UserDto;
import com.evox.evoxbackend.exception.CustomException;
import com.evox.evoxbackend.repository.UserRepositoryy;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class UserServices {
    private final UserRepositoryy userRepositoryy;
    private final ModelMapper modelMapper;

    @Transactional
    public Flux<MultiLevelDto> getAllMultilevel(Integer id) {
        return   Mono.justOrEmpty(userRepositoryy.findUserAndDescendants(id))
                .flatMapMany(Flux::fromIterable).filter(ele->!ele.getId().equals(id))
                 .map(ele-> new MultiLevelDto(ele.getParent().getFullName(),ele.getRefLink(), ele.getUsername(),ele.getFullName() , ele.getStatus() , ele.getCreatedAt() ))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND,"An error has occurred please contact the administrator")));

    }



    public Flux<UserDto> getAllUsers(){
        return Flux.fromIterable(userRepositoryy.findAll()).map(ele->modelMapper.map(ele, UserDto.class));
    }

}
