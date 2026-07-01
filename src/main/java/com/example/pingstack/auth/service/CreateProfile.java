package com.example.pingstack.auth.service;


import com.cloudinary.Cloudinary;
import com.example.pingstack.auth.dto.CreateProfileRequest;
import com.example.pingstack.auth.mapper.ProfileMapper;
import com.example.pingstack.auth.model.Profile;
import com.example.pingstack.auth.model.User;
import com.example.pingstack.auth.repository.ProfileRepository;
import com.example.pingstack.wallet.utils.ExtractUserInfoFromAuthHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateProfile {


    @Autowired
    private ExtractUserInfoFromAuthHeader extractUserInfoFromAuthHeader;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapper profileMapper;







    public void createProfile (String authHeader, CreateProfileRequest createProfileRequest){
        User user = extractUserInfoFromAuthHeader.extractUserInfo(authHeader);
        Optional<Profile> existingProfile = profileRepository.findByUser(user);
        if(existingProfile.isPresent()){
            throw new RuntimeException("Profile Exist for this user");
        }
        createProfileRequest.setEmail(user.getEmail());
        createProfileRequest.setFirstName(user.getFirstName());
        createProfileRequest.setSector(user.getSector());
        Profile newProfile = ProfileMapper.toEntity(createProfileRequest);
        newProfile.setUser(user);
//        uploadKycImageToCloudinary(multipartFile,newProfile);


    }
}
