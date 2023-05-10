package com.evox.evoxbackend.controller;

import com.evox.evoxbackend.model.Session;
import com.evox.evoxbackend.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(path = "/api/session")
@RequiredArgsConstructor
public class SessionController {
    private final SessionRepository sessionRepository;

    @GetMapping(path = "/prueba")
    public ResponseEntity<Flux<Session>> authenticate(){
         var data = Flux.fromIterable(sessionRepository.findAll());
        return  ResponseEntity.ok(data);
    }

}
