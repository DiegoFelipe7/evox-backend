package com.evox.evoxbackend.model;
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
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String ipAddress;
    private String country;
    private LocalDateTime userLogin;
    @PrePersist
    protected void onCreate() {
        userLogin = LocalDateTime.now();
    }
}
