package com.example.pingstack.compliance.repository;

import com.example.pingstack.auth.model.User;
import com.example.pingstack.compliance.enums.SenderIdStatus;
import com.example.pingstack.compliance.model.SenderId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SenderIdRepository extends JpaRepository<SenderId , UUID> {
    Optional<SenderId> findByUser(User user);
    Page<SenderId> findByUser(User user, Pageable pageable);
    @Query("SELECT s FROM SenderId s WHERE s.user = :user AND s.status = :status")
    Page<SenderId> findSenderIds(@Param("user") User user, @Param("status") SenderIdStatus status, Pageable pageable);

}
