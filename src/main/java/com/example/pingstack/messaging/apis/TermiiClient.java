package com.example.pingstack.messaging.apis;


import com.example.pingstack.messaging.dtos.TermiiRequestDto;
import com.example.pingstack.messaging.dtos.TermiiResponseDto;
import com.example.pingstack.messaging.enums.MessageChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class TermiiClient {

    private final RestClient restClient;


    @Value("${termii.api.key}")
    private String apiKey;
    @Value("${termii.base.url:https://api.ng.termii.com}")
    private String baseUrl;

    public TermiiClient() {
        restClient = null;
    }


    public TermiiResponseDto disPatchSmsChunk (String senderId, List<String> batchRecipients, String sms , boolean isTransactional){


        TermiiRequestDto payload = new TermiiRequestDto(
                apiKey,
                batchRecipients,
                senderId,
                sms,
                "plain",
                isTransactional ? MessageChannel.dnd : MessageChannel.generic
        );

        try {
            assert restClient != null;
            return restClient.post()
                    .uri(baseUrl + "/api/sms/send")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .body(TermiiResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
