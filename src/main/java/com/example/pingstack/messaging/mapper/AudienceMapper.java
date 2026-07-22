package com.example.pingstack.messaging.mapper;

import com.example.pingstack.messaging.dtos.AudienceResponse;
import com.example.pingstack.messaging.dtos.CreateAudienceRequest;
import com.example.pingstack.messaging.model.AudienceList;
import org.springframework.stereotype.Component;

@Component
public class AudienceMapper {


    public AudienceList toEntity (CreateAudienceRequest createAudienceRequest){
        AudienceList audienceList = new AudienceList();
        audienceList.setName(createAudienceRequest.getName());
        return audienceList;
    }

    public AudienceResponse toResponse (AudienceList audienceList){
        return new AudienceResponse(
                audienceList.getId(),
                audienceList.getName(),
                audienceList.getCreatedAt().toString(),
                audienceList.getUpdatedAt() != null
                        ? audienceList.getUpdatedAt().toString()
                        : audienceList.getCreatedAt().toString()
        );
    }
}
