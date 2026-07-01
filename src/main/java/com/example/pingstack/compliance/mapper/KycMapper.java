package com.example.pingstack.compliance.mapper;


import com.example.pingstack.compliance.dto.KycUploadResponse;
import com.example.pingstack.compliance.model.Kyc;
import org.springframework.stereotype.Component;

@Component
public class KycMapper {

    public KycUploadResponse toResponse (Kyc kyc){
        return new KycUploadResponse(
                kyc.getKycId(),
                kyc.getUser().getEmail(),
                kyc.getStatus(),
                kyc.getKycImageUrl(),
                kyc.getKycImageType() ,
                kyc.getKycImageName()
        );
    }
}
