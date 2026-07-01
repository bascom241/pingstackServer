package com.example.pingstack.auth.controller;


import com.example.pingstack.auth.dto.*;
import com.example.pingstack.auth.service.UserDetailServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserDetailServiceImplementation userDetailServiceImplementation;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto){
        RegisterResponseDto data = userDetailServiceImplementation.registerUser(registerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login (@RequestBody LoginRequestDto loginRequestDto){
        LoginResponseDto data = userDetailServiceImplementation.login(loginRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh (@RequestBody RefreshTokenRequest refreshTokenRequest){
        LoginResponseDto data = userDetailServiceImplementation.refreshToken(refreshTokenRequest);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }
}
