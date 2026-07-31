package com.example.pingstack.email.mapper;

import com.example.pingstack.email.dto.EmailConfigResponse;
import com.example.pingstack.email.dto.UpdatePreferenceResponse;
import com.example.pingstack.email.model.EmailConfig;
import org.springframework.stereotype.Component;


@Component
public class EmailConfigMapper {

    public EmailConfigResponse toResponse (EmailConfig emailConfig){
        return new EmailConfigResponse(
                emailConfig.getApiKeyHash(),
                emailConfig.getApiKeyPrefix(),
                emailConfig.getCustomDomain(),
                emailConfig.isTrackOpens(),
                emailConfig.isTrackClicks(),
                emailConfig.isBounceAlerts(),
                emailConfig.getEmailsSentThisMonth(),
                emailConfig.getMonthlyLimit()
        );
    }

    public UpdatePreferenceResponse toPreferenceResponse ( EmailConfig emailConfig){
        return new UpdatePreferenceResponse(
                emailConfig.isTrackOpens(),
                emailConfig.isTrackClicks(),
                emailConfig.isBounceAlerts()
        );
    }
}
