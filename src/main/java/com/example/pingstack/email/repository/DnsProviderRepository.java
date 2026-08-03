package com.example.pingstack.email.repository;

import com.example.pingstack.email.model.domain.BrevoDomain;
import com.example.pingstack.email.model.domain.DnsRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DnsProviderRepository extends JpaRepository<DnsRecord , UUID> {
    Optional<List<DnsRecord>> findByBrevoDomain(BrevoDomain brevoDomain);
}
