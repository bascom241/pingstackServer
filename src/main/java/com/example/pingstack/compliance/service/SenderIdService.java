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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Page<SenderIdResponse> getSenderIds(String authHeader, int page, int size){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<SenderId> senderIds = senderIdRepository.findByUser(user, pageable);
        return senderIds.map(senderIdMapper::toResponse);
    }

    public Page<SenderIdResponse> getApprovedSenderId(String authHeader, int page, int size){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<SenderId> approvedSenderIds = senderIdRepository.findSenderIds(user,SenderIdStatus.Approved, pageable);
        return approvedSenderIds.map(senderIdMapper::toResponse);
    }

}
