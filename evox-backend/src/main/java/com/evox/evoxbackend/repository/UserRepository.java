package com.evox.evoxbackend.repository;

import com.evox.evoxbackend.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDto, Integer> {
    Optional<UserDto> findByUsername(String username);
}
