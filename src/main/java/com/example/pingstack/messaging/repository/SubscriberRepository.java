package com.example.pingstack.messaging.repository;

import com.example.pingstack.messaging.model.Subscriber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;

public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {

    @Query("SELECT s FROM Subscriber s WHERE s.audienceList.id = :listId AND " +
            "(:query IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR s.destinationNumber LIKE CONCAT('%', :query, '%'))")
    Page<Subscriber> findByListAndQuery(
            @Param("listId") UUID listId,
            @Param("query") String query,
            Pageable pageable
    );
}