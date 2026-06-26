package com.example.pingstack.auth.service;

import com.example.pingstack.auth.model.RefreshToken;
import com.example.pingstack.auth.model.User;
import com.example.pingstack.auth.repository.RefreshTokenRepository;
import com.example.pingstack.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;


@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public RefreshToken createToken(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));


        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);

        RefreshToken refreshToken;

        if(existingToken.isPresent()){
            refreshToken = existingToken.get();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plus(30, ChronoUnit.DAYS));
        }else {
            refreshToken = new RefreshToken();
            refreshToken.setUser(user );
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plus(30, ChronoUnit.DAYS));
        }

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findToken (String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken validateToken(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())< 0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh Token Expired: Please login Again");
        }
        return token;
    }

}
