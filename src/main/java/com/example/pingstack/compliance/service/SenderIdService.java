package com.example.pingstack.compliance.service;


import com.example.pingstack.auth.model.User;
import com.example.pingstack.compliance.dto.SenderIdRequest;
import com.example.pingstack.compliance.dto.SenderIdResponse;
import com.example.pingstack.compliance.enums.SenderIdStatus;
import com.example.pingstack.compliance.mapper.SenderIdMapper;
import com.example.pingstack.compliance.model.SenderId;
import com.example.pingstack.compliance.repository.SenderIdRepository;
import com.example.pingstack.wallet.utils.ExtractUserInfoFromAuthHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SenderIdService {

    @Autowired
    private ExtractUserInfoFromAuthHeader extractUserInfoFromAuthHeader;

    @Autowired
    private SenderIdMapper senderIdMapper;

    @Autowired
    private SenderIdRepository senderIdRepository;


    public SenderIdResponse registerSenderId(String authHeader , SenderIdRequest senderIdRequest){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        SenderId senderId = senderIdMapper.toEntity(senderIdRequest);
        senderId.setUser(user);
        senderId.setStatus(SenderIdStatus.Pending);
        SenderId senderToSave = senderIdRepository.save(senderId);
        return senderIdMapper.toResponse(senderToSave);
    }


}
