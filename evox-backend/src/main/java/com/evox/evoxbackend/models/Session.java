package com.evox.evoxbackend.models;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Session {
    private Integer id;
    private User user;
    private String ipAddress;
    private String country;
    private LocalDateTime userLogin;
}
