package com.example.pingstack.auth.repository;

import com.example.pingstack.auth.model.RefreshToken;
import com.example.pingstack.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken , UUID> {
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByToken(String token);
}
