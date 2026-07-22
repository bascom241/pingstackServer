package com.example.pingstack.messaging.controller;


import com.example.pingstack.messaging.enums.DeliveryStatus;
import com.example.pingstack.messaging.model.MessagingLog;
import com.example.pingstack.messaging.repository.MessagingLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/callbacks")
public class DeliveryReportController {


    @Autowired
    private MessagingLogRepository messagingLogRepository;

    @PostMapping("/termii-dlr")
    public ResponseEntity<Void> processIncomingTermiiDlrReport(@RequestBody Map<String, Object> callBackPayLoad){
        asyncProcessDlrData(callBackPayLoad);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @Async("dlrExecutor")
    protected void asyncProcessDlrData(Map<String,Object> payload){

        try {
            if(payload.containsKey("results")){
                List<Map<String, Object>> records = (List<Map<String, Object>>) payload.get("results");
                for(Map<String, Object> data : records){
                    updateStatus(data);
                }
            }else {
                updateStatus(payload);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    private void updateStatus(Map<String,Object> node){
        String trackingId = (String) node.get("message_id");
        String reportStatus = (String) node.get("status");

        if (trackingId == null) return;

        Optional<MessagingLog> targetLog = messagingLogRepository.findByApiMessageId(trackingId);

        if(targetLog.isPresent()) {
            MessagingLog logEntity = targetLog.get();

            if ("Delivered".equalsIgnoreCase(reportStatus)) {
                logEntity.setStatus(DeliveryStatus.DELIVERED);
                logEntity.setStatus(DeliveryStatus.DELIVERED);
            } else if ("Rejected".equalsIgnoreCase(reportStatus) || "Failed".equalsIgnoreCase(reportStatus)) {
                logEntity.setStatus(DeliveryStatus.FAILED);
                logEntity.setErrorCode(String.valueOf(node.get("reason")));

            }
            messagingLogRepository.save(logEntity);
        }
    }

}
