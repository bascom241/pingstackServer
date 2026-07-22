package com.example.pingstack.compliance.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.pingstack.compliance.enums.KycApproval;
import com.example.pingstack.auth.model.User;
import com.example.pingstack.compliance.dto.KycUploadResponse;
import com.example.pingstack.compliance.mapper.KycMapper;
import com.example.pingstack.compliance.model.Kyc;
import com.example.pingstack.compliance.repository.KycRepository;
import com.example.pingstack.wallet.utils.ExtractUserInfoFromAuthHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class KycDocumentService {


    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ExtractUserInfoFromAuthHeader extractUserInfoFromAuthHeader;

    @Autowired
    private KycMapper kycMapper;

    @Autowired
    private KycRepository kycRepository;

    private void uploadKycImageToCloudinary(MultipartFile multipartFile, Kyc kyc){
        if(multipartFile != null && !multipartFile.isEmpty()){
            Map<?,?> result;
            try {
                result = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            kyc.setKycImageName(multipartFile.getOriginalFilename());
            kyc.setKycImageType(multipartFile.getContentType());
            kyc.setKycImageUrl(result.get("secure_url").toString());
        }else {
            throw new RuntimeException("Image file is empty");
        }
    }

    public KycUploadResponse uploadKyc (String authHeader, MultipartFile file){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        Optional<Kyc> exitingKyc = kycRepository.findByUser(user);
        if(exitingKyc.isPresent()){
            throw new RuntimeException("kyc already exist, cant upload new one");
        }
        Kyc kyc = new Kyc();
        kyc.setUser(user);
        kyc.setStatus(KycApproval.Pending);
        uploadKycImageToCloudinary(file, kyc);
        kycRepository.save(kyc);
        return kycMapper.toResponse(kyc);
    }









}
