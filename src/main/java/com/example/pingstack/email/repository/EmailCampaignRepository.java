package com.example.pingstack.email.repository;

import com.example.pingstack.auth.model.User;
import com.example.pingstack.email.model.EmailCampaign;
import jakarta.validation.constraints.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailCampaignRepository extends JpaRepository<EmailCampaign, UUID> {
    @Query("SELECT c FROM EmailCampaign c WHERE c.user = :user")
    Page<EmailCampaign> findById(@Param("user") User user, Pageable pageable);
}
