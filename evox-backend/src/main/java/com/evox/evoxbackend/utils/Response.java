package com.evox.evoxbackend.utils;

import com.evox.evoxbackend.utils.enums.TypeStateResponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Response {
    private TypeStateResponse typeStatus;
    private String message;
}
