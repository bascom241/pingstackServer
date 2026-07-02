package com.example.pingstack.compliance.mapper;


import com.example.pingstack.compliance.dto.SenderIdRequest;
import com.example.pingstack.compliance.dto.SenderIdResponse;
import com.example.pingstack.compliance.model.SenderId;
import org.springframework.stereotype.Component;

@Component
public class SenderIdMapper {


    public SenderId toEntity (SenderIdRequest senderIdRequest){
        SenderId senderId = new SenderId();
        senderId.setSenderId(senderIdRequest.getSenderId());
        senderId.setCompanyName(senderIdRequest.getCompanyName());
        senderId.setUseCaseSample(senderIdRequest.getUseCaseSample());
        return senderId;
    }

    public SenderIdResponse toResponse(SenderId senderId){
        return new SenderIdResponse(
                senderId.getSenderId(),
                senderId.getCompanyName(),
                senderId.getUseCaseSample(),
                senderId.getStatus()
        );
    }
}
