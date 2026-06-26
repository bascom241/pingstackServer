package com.example.pingstack.auth.mapper;

import com.example.pingstack.auth.dto.RegisterRequestDto;
import com.example.pingstack.auth.dto.RegisterResponseDto;
import com.example.pingstack.auth.model.User;
import org.springframework.stereotype.Component;

@Component
public class RegisterMapper {

    public static User toEntity (RegisterRequestDto registerRequestDto){
        User user = new User();
        user.setEmail(registerRequestDto.getEmail());
        user.setCompanyName(registerRequestDto.getCompanyName());
        user.setCreatorRole(registerRequestDto.getCreatorRole());
        user.setPassword(registerRequestDto.getPassword());
        user.setFirstName(registerRequestDto.getFirstName());
        user.setLastName(registerRequestDto.getLastName());
        user.setSource(registerRequestDto.getSource());
        user.setSector(registerRequestDto.getSector());
        user.setPhoneNumber(registerRequestDto.getPhoneNumber());
        return user;
    }

    public static RegisterResponseDto toResponse(User user){
        return new RegisterResponseDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCompanyName(),
                user.getPhoneNumber(),
                user.getCompanyName(),
                user.getSector(),
                user.getCreatorRole(),
                user.getSource()
        );
    }
}
