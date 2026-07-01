package com.example.pingstack.compliance.repository;

import com.example.pingstack.auth.model.User;
import com.example.pingstack.compliance.model.Kyc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface KycRepository extends JpaRepository<Kyc, UUID> {

    Optional<Kyc> findByUser(User user);
}
