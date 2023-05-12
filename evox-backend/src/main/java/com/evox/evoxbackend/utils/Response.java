package com.evox.evoxbackend.utils;

import com.evox.evoxbackend.utils.enums.TypeStateResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Response {
    private TypeStateResponse typeStatus;
    private String message;
}
