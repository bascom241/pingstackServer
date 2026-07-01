package com.example.pingstack.compliance.repository;

import com.example.pingstack.auth.model.User;
import com.example.pingstack.compliance.model.SenderId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SenderIdRepository extends JpaRepository<SenderId , UUID> {

    Optional<SenderId> findByUser(User user);
}
