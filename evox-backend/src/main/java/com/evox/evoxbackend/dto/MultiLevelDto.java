package com.evox.evoxbackend.dto;

import com.evox.evoxbackend.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MultiLevelDto {
    private String parent;
    private String refLink;
    private String userName;
    private String fullName;
    private Boolean status;
    private LocalDateTime dateRegistered;



}
