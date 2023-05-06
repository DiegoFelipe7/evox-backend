package com.evox.evoxbackend.dto;

import com.evox.evoxbackend.models.Role;
import com.evox.evoxbackend.models.User;
import com.evox.evoxbackend.models.enums.TypeOfIdentification;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserDto  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = true)
    private String username;
    private String fullName;
    private TypeOfIdentification typeOfIdentification;
    private String identification;
    private String phone;
    private String country;
    private String countryOfResidence;
    private Boolean emailVerified;
    private String token;
    private String photo;
    private String refLink;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private UserDto parent;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserDto> listChildren;

    @OneToMany(fetch = FetchType.LAZY , mappedBy = "user", cascade = CascadeType.ALL)
    private List<SessionDto> sessionDtoList;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
