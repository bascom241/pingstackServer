package com.example.pingstack.messaging.repository;


import com.example.pingstack.messaging.dtos.AudienceListSummary;
import com.example.pingstack.messaging.model.AudienceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;
import java.util.List;

public interface AudienceListRepository extends JpaRepository<AudienceList, UUID> {

    @Query("SELECT l.id as id, l.name as name, l.updatedAt as updatedAt, COUNT(s) as totalCount " +
            "FROM AudienceList l " +
            "LEFT JOIN Subscriber s ON s.audienceList = l " +
            "WHERE l.user.id = :userId " +
            "GROUP BY l.id, l.name, l.updatedAt")
    List<AudienceListSummary> findAudienceListByUserId(@Param("userId") UUID userId);

}