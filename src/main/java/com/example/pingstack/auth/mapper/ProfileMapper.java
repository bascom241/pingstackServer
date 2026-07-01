package com.example.pingstack.auth.mapper;


import com.example.pingstack.auth.dto.CreateProfileRequest;
import com.example.pingstack.auth.dto.CreateProfileResponse;
import com.example.pingstack.auth.model.Profile;
import com.example.pingstack.auth.model.User;
import com.example.pingstack.auth.service.CreateProfile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {



    public static Profile toEntity (CreateProfileRequest createProfileRequest){
        Profile profile = new Profile();
        profile.setEmail(createProfileRequest.getEmail());
        profile.setFirstName(createProfileRequest.getFirstName());
        profile.setLastName(createProfileRequest.getLastName());
        profile.setSector(createProfileRequest.getSector());
        return profile;
    }

    public CreateProfileResponse toResponse(Profile profile){
        return new CreateProfileResponse(
                profile.getProfileId(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getEmail(),
                profile.getSector()
        );
    }
}
