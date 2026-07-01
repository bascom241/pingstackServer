package com.example.pingstack.wallet.repository;

import com.example.pingstack.auth.model.User;
import com.example.pingstack.wallet.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByTransactionNumber(String reference);
    Page<Order> findOrderByUser(User user, Pageable pageable);
    List<Order> findOrderByUser(User user);
}
