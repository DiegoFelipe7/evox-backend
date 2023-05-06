package com.evox.evoxbackend.models;

import com.evox.evoxbackend.dto.SessionDto;
import com.evox.evoxbackend.models.enums.TypeOfIdentification;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Integer id;
    private String email;
    private String password;
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
    private User parent;
    private List<User> listChildren;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
