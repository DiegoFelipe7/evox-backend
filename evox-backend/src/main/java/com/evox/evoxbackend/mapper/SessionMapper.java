package com.evox.evoxbackend.mapper;

import com.evox.evoxbackend.dto.SessionDto;
import com.evox.evoxbackend.models.Session;

public class SessionMapper {
    private SessionMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Session sessionDtoSession(SessionDto sessionDto){
        return Session.builder()
                .id(sessionDto.getId())
                .user(UserMapper.userDtoUser(sessionDto.getUser()))
                .ipAddress(sessionDto.getIpAddress())
                .country(sessionDto.getCountry())
                .userLogin(sessionDto.getUserLogin())
                .build();
    }

    public static SessionDto sessionToSessionDto(Session session){
        return SessionDto.builder()
                .id(session.getId())
                .user(UserMapper.userToUserDTO(session.getUser()))
                .ipAddress(session.getIpAddress())
                .country(session.getCountry())
                .userLogin(session.getUserLogin())
                .build();
    }
}
