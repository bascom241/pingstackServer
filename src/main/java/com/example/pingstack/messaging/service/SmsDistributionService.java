package com.example.pingstack.messaging.service;


import com.example.pingstack.auth.model.User;
import com.example.pingstack.auth.repository.UserRepository;
import com.example.pingstack.messaging.apis.TermiiClient;
import com.example.pingstack.messaging.dtos.TermiiResponseDto;
import com.example.pingstack.messaging.enums.DeliveryStatus;
import com.example.pingstack.messaging.model.MessagingLog;
import com.example.pingstack.messaging.repository.MessagingLogRepository;
import com.example.pingstack.wallet.service.WalletService;
import com.example.pingstack.wallet.service.WalletTopUpService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SmsDistributionService {


    @Autowired
    private WalletTopUpService walletTopUpService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessagingLogRepository logRepository;

    @Autowired
    private TermiiClient termiiClient;


    @Async("bulkSmsTaskExecutor")
    @Transactional
    public void executeBulkPipeline(UUID clientId, String senderId, List<String> recipients , String messageBody, boolean isTransactional){
        BigDecimal pricePerUnit = isTransactional ? new BigDecimal("4.50") : new BigDecimal("2.50");
        BigDecimal totalCost = pricePerUnit.multiply(BigDecimal.valueOf(recipients.size()));
        walletTopUpService.debitWalletBalance(clientId,totalCost);
        User user = userRepository.findById(clientId).orElseThrow(() -> new RuntimeException("client id not found"));

        List<MessagingLog> logsToSave = new ArrayList<>();

        for(String phone : recipients){
            MessagingLog log = new MessagingLog();
            log.setRecipient(phone);
            log.setClientId(user);
            log.setCostCharged(pricePerUnit);
            log.setMessageBody(messageBody);
            log.setStatus(DeliveryStatus.SENT);
            log.setSenderId(senderId);
        }

        List<MessagingLog> savedLogs = logRepository.saveAll(logsToSave);
        int chunkSize = 100;
        for (int i = 0; i < savedLogs.size(); i += chunkSize ){
            List<MessagingLog> chunckedLogs = savedLogs.subList(i, Math.min(i + chunkSize, savedLogs.size()));
            List<String> chunckedPhoneNumbers = savedLogs.stream().map(MessagingLog::getRecipient).toList();

            try {
                TermiiResponseDto response = termiiClient.disPatchSmsChunk(senderId, chunckedPhoneNumbers, messageBody, isTransactional);

                if(response != null && response.getMessageId() != null){
                    for(MessagingLog logElement : chunckedLogs){
                        logElement.setApiMessageId(response.getMessageId());
                    }
                    logRepository.saveAll(chunckedLogs);
                }
            } catch (Exception e) {
                // If network falls, update status to FAILED to allow audits/refund engines
                for (MessagingLog logElement : chunckedLogs) {
                    logElement.setStatus(DeliveryStatus.FAILED);
                    logElement.setErrorCode("HTTP_TIMEOUT_GATEWAY");
                }
                logRepository.saveAll(chunckedLogs);
            }
        }

    }
}
