package com.example.pingstack.wallet.utils;


import com.example.pingstack.auth.model.User;
import com.example.pingstack.auth.repository.UserRepository;
import com.example.pingstack.auth.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExtractUserInfoFromAuthHeader {


    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    public User extractUserInfo (String authHeader){
        String token = authHeader.substring(7);
        String email = jwtService.extractUserName(token);
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("email not found for this AuthHeader"));
    }
}
