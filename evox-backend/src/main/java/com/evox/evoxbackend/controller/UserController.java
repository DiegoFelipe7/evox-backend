package com.evox.evoxbackend.controller;

import com.evox.evoxbackend.dto.MultiLevelDto;
import com.evox.evoxbackend.dto.UserDto;
import com.evox.evoxbackend.model.User;
import com.evox.evoxbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping(path = "/unilevel/{id}")
    public ResponseEntity<Flux<MultiLevelDto>> getAllUnilevel(@PathVariable("id") Integer id){
        var user = userService.getAllMultilevel(id);
        return ResponseEntity.ok(user);
    }


    @GetMapping
    public ResponseEntity<Flux<UserDto>> getAllListUser(){
        var data = userService.getAllUsers();
        return ResponseEntity.ok(data);
    }




}
