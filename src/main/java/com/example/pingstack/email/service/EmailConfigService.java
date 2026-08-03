package com.example.pingstack.email.service;


import com.example.pingstack.auth.model.User;
import com.example.pingstack.email.dto.ApiKeyGeneratedResponse;
import com.example.pingstack.email.dto.UpdatePreferenceRequest;
import com.example.pingstack.email.dto.UpdatePreferenceResponse;
import com.example.pingstack.email.repository.EmailConfigRepository;
import com.example.pingstack.email.dto.EmailConfigResponse;
import com.example.pingstack.email.mapper.EmailConfigMapper;
import com.example.pingstack.email.model.EmailConfig;
import com.example.pingstack.email.util.ApiKeyUtil;
import com.example.pingstack.wallet.utils.ExtractUserInfoFromAuthHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailConfigService {

    @Autowired
    private ExtractUserInfoFromAuthHeader extractUserInfoFromAuthHeader;

    @Autowired
    private EmailConfigRepository emailConfigRepository;

    @Autowired
    private EmailConfigMapper emailConfigMapper;

    @Autowired
    private ApiKeyUtil apiKeyUtil;

    public EmailConfigResponse createOrGetEmailConfig (String authHeader){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        EmailConfig emailConfig = emailConfigRepository
                .findByUserId(user.getUserId())
                .orElseGet(() -> {

                    EmailConfig config = new EmailConfig();
                    config.setUserId(user.getUserId());

                    String rawApiKey = apiKeyUtil.generateRawKey();
                    config.setApiKeyHash(apiKeyUtil.hashKey(rawApiKey));
                    config.setApiKeyPrefix(apiKeyUtil.extractPrefix(rawApiKey));

                    return emailConfigRepository.save(config);
                });

        return emailConfigMapper.toResponse(emailConfig);
    }

    private String generateApiKey(){
        return "ps_live_" + UUID.randomUUID().toString().replace("-", "");
    }

    public ApiKeyGeneratedResponse regenerateKey(String authHeader){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        EmailConfig emailConfigResult = emailConfigRepository.findByUserId(user.getUserId()).orElseGet(() -> {
            EmailConfig emailConfig = new EmailConfig();
            emailConfig.setUserId(user.getUserId());
            return emailConfig;
        });
        String rawApiKey = apiKeyUtil.generateRawKey();
        String hashedKey = apiKeyUtil.hashKey(rawApiKey);
        String prefix = apiKeyUtil.extractPrefix(rawApiKey);
        emailConfigResult.setApiKeyHash(hashedKey);
        emailConfigResult.setApiKeyPrefix(prefix);
        emailConfigRepository.save(emailConfigResult);
        return new ApiKeyGeneratedResponse(rawApiKey,prefix);
    }

    public UpdatePreferenceResponse updateUserPreference (String authHeader, UpdatePreferenceRequest updatePreferenceRequest){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        EmailConfig emailConfig = emailConfigRepository.findByUserId(user.getUserId()).orElseThrow(() -> new RuntimeException("Email Config not found for this user"));
        emailConfig.setTrackClicks(updatePreferenceRequest.isTrackClicks());
        emailConfig.setTrackOpens(updatePreferenceRequest.isTrackOpens());
        emailConfig.setBounceAlerts(updatePreferenceRequest.isBounceAlerts());
        emailConfigRepository.save(emailConfig);
        return emailConfigMapper.toPreferenceResponse(emailConfig);
    }

}
