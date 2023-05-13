package com.evox.evoxbackend.services;

import com.evox.evoxbackend.dto.MultiLevelDto;
import com.evox.evoxbackend.dto.UserDto;
import com.evox.evoxbackend.exception.CustomException;
import com.evox.evoxbackend.repository.UserRepository;

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

    public Flux<UserDto> getAllUsers(){
        return repository.findAll()
                .map(ele->modelMapper.map(ele, UserDto.class));
    }

    public Flux<MultiLevelDto> getAllMultilevel(Integer id) {
       return    repository.findUserAndDescendants(id)
                 .filter(ele->!ele.getId().equals(id))
                 .flatMap(data->{
                    return repository.findById(data.getId()).map(ele->{
                         return new MultiLevelDto(ele.getFullName(),ele.getRefLink(), ele.getUsername(),ele.getFullName() , ele.getStatus() , ele.getCreatedAt() );

                     });
                })
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST,"An error has occurred please contact the administrator", TypeStateResponse.Error)));
        //vara prueba= repository.findUserAndDescendants(id)
    }

}
