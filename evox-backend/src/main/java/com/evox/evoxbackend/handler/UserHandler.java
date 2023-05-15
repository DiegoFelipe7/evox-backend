package com.evox.evoxbackend.handler;

import com.evox.evoxbackend.dto.MultiLevelDto;
import com.evox.evoxbackend.services.UserServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
@Slf4j
@RequiredArgsConstructor
public class UserHandler {
    private final UserServices userServices;


  public Mono<ServerResponse> unilevelUsers(ServerRequest serverRequest){
       String token = serverRequest.headers().firstHeader("Authorization");
       return ServerResponse.ok()
                  .contentType(MediaType.APPLICATION_JSON)
                  .body(userServices.getAllMultilevel(token) , MultiLevelDto.class);


    }
}
