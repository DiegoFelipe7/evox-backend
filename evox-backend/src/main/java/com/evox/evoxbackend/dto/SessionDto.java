package com.evox.evoxbackend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionDto {
    private Integer id;
    private UserDto userDto;
    private String ipAddress;
    private String country;
    private LocalDateTime userLogin;
}
