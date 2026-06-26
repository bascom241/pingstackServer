package com.example.pingstack.wallet.repository;

import com.example.pingstack.auth.model.User;
import com.example.pingstack.wallet.model.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.user.userId = :userId")
    Wallet findWalletForUpdate(@Param("userId") UUID userId);
    Optional<Wallet> findWalletByUser(User user);

}
