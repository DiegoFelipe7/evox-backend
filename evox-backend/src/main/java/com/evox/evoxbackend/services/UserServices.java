package com.evox.evoxbackend.services;

import com.evox.evoxbackend.dto.UserDto;
import com.evox.evoxbackend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;



@Service
@RequiredArgsConstructor
public class UserServices {
    private final UserRepository repository;
    private final ModelMapper modelMapper;

    public Flux<UserDto> getAllUsers(){
        return repository.findAll()
                .map(ele->modelMapper.map(ele, UserDto.class));
    }

}
