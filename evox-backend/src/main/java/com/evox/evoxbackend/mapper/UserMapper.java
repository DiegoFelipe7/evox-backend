package com.evox.evoxbackend.mapper;

import com.evox.evoxbackend.dto.UserDto;
import com.evox.evoxbackend.models.User;

public class UserMapper {
    private UserMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static User userDtoUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .username(userDto.getUsername())
                .fullName(userDto.getFullName())
                .typeOfIdentification(userDto.getTypeOfIdentification())
                .identification(userDto.getIdentification())
                .phone(userDto.getPhone())
                .country(userDto.getCountry())
                .countryOfResidence(userDto.getCountryOfResidence())
                .emailVerified(userDto.getEmailVerified())
                .token(userDto.getToken())
                .photo(userDto.getPhoto())
                .refLink(userDto.getRefLink())
                .parent(UserMapper.userDtoUser(userDto.getParent()))
                .build();
    }

    public static UserDto userToUserDTO(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .typeOfIdentification(user.getTypeOfIdentification())
                .identification(user.getIdentification())
                .phone(user.getPhone())
                .country(user.getCountry())
                .countryOfResidence(user.getCountryOfResidence())
                .emailVerified(user.getEmailVerified())
                .token(user.getToken())
                .photo(user.getPhoto())
                .refLink(user.getRefLink())
                .parent(UserMapper.userToUserDTO(user.getParent()))
                .build();
    }
}
