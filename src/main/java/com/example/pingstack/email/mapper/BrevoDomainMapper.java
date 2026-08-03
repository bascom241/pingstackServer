package com.example.pingstack.email.mapper;


import com.example.pingstack.email.dto.brevo.BrevoDomainDto;
import com.example.pingstack.email.dto.brevo.DnsRecordDto;
import com.example.pingstack.email.model.domain.BrevoDomain;
import com.example.pingstack.email.model.domain.DnsRecord;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BrevoDomainMapper {


    public BrevoDomain toBrevo(BrevoDomainDto brevoDomainDto){
        BrevoDomain brevoDomain = new BrevoDomain();
        brevoDomain.setBrevoDomainId(brevoDomainDto.getId());
        brevoDomain.setDomainName(brevoDomainDto.getDomain_name());
        brevoDomain.setDomainProvider(brevoDomainDto.getDomain_provider());
        brevoDomain.setDnsRecords(convertToDnsRecord(brevoDomainDto.getDns_records(), brevoDomain));
        brevoDomain.setMessage(brevoDomainDto.getMessage());
        return brevoDomain;
    }

    private List<DnsRecord> convertToDnsRecord(Map<String , DnsRecordDto> records, BrevoDomain brevoDomain){
        return records.entrySet()
                .stream()
                .map(entry -> {
                    DnsRecord record = new DnsRecord();
                    record.setRecordName(entry.getKey());
                    DnsRecordDto dto = entry.getValue();
                    record.setType(dto.getType());
                    record.setHostName(dto.getHost_name());
                    record.setValue(dto.getValue());
                    record.setVerified(dto.isStatus());
                    record.setBrevoDomain(brevoDomain);
                    return record;
                })
                .toList();
    }
}
