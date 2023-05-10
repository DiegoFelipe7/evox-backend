package com.evox.evoxbackend.security.repository;

import com.evox.evoxbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AuthRepository extends JpaRepository<User , Integer> {

    User findByUsernameOrEmail(String username, String email);
    User findByUsernameIgnoreCase(String username);

}
