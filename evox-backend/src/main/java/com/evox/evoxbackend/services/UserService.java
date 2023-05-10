package com.evox.evoxbackend.services;

import com.evox.evoxbackend.dto.MultiLevelDto;
import com.evox.evoxbackend.dto.UserDto;
import com.evox.evoxbackend.exception.LocalNotFoundException;
import com.evox.evoxbackend.model.User;
import com.evox.evoxbackend.repository.UserRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Flux<MultiLevelDto> getAllMultilevel(Integer id) {
        return   Mono.justOrEmpty(userRepository.findUserAndDescendants(id))
                .flatMapMany(Flux::fromIterable).filter(ele->!ele.getId().equals(id))
                 .map(ele->{
                    return new MultiLevelDto(ele.getParent().getFullName(),ele.getRefLink(), ele.getUsername(),ele.getFullName() , ele.getStatus() , ele.getCreatedAt() );
                 })
                .switchIfEmpty(Mono.error(new LocalNotFoundException("An error has occurred please contact the administrator")));

    }

    public Flux<UserDto> getAllUsers(){
        return Flux.fromIterable(userRepository.findAll()).map(ele->modelMapper.map(ele, UserDto.class));
    }

}
