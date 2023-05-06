package com.evox.evoxbackend.dto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "session")
@Builder
public class SessionDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private UserDto user;
    private String ipAddress;
    private String country;
    private LocalDateTime userLogin;

    @PrePersist
    protected void onCreate() {
        userLogin = LocalDateTime.now();
    }
}
