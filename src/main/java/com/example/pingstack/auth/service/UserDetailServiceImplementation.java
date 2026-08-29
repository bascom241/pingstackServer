package com.example.pingstack.auth.service;

import com.example.pingstack.auth.dao.UserPrincipal;
import com.example.pingstack.auth.dto.*;
import com.example.pingstack.auth.mapper.RegisterMapper;
import com.example.pingstack.auth.model.RefreshToken;
import com.example.pingstack.auth.model.Role;
import com.example.pingstack.auth.model.User;
import com.example.pingstack.auth.repository.RefreshTokenRepository;
import com.example.pingstack.auth.repository.RoleRepository;
import com.example.pingstack.auth.repository.UserRepository;
import com.example.pingstack.exceptions.auth.UserAlreadyExistException;
import com.example.pingstack.mail.EmailService;
import com.example.pingstack.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailServiceImplementation implements UserDetailsService {



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegisterMapper registerMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;


    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private EmailService emailService;


    @Autowired
    private CreateProfile createProfile;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));
        return new UserPrincipal(user);
    }


    public RegisterResponseDto registerUser (RegisterRequestDto requestDto) {
        Optional<User> userExist = userRepository.findByEmail(requestDto.getEmail());
        if(userExist.isPresent()){
            throw new UserAlreadyExistException("user already exists");
        }
        User newUser = RegisterMapper.toEntity(requestDto);
        String encodedPassword = new BCryptPasswordEncoder().encode(requestDto.getPassword());
        newUser.setPassword(encodedPassword);
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
        newUser.setVerificationCode(otp);
        newUser.setVerified(false);
        newUser.setResetPasswordExpiry(Instant.now().plusSeconds(300));

        if(requestDto.getRole() != null){
            String userPassedRole = requestDto.getRole();
            Role role = roleRepository.findByRoleName(userPassedRole).orElseThrow(()-> new RuntimeException("role not found"));
            newUser.setRoles(new HashSet<>(Set.of(role)));

        }else {
            Role role = roleRepository.findByRoleName("user").orElseThrow(()-> new RuntimeException("role not found"));
            newUser.setRoles(new HashSet<>(Set.of(role)));
        }

        User savedUser = userRepository.save(newUser);

        try {
            walletService.createWallet(savedUser);
            savedUser.setWalletCreated(true);
            userRepository.save(savedUser);
        }catch(Exception e){
            savedUser.setWalletCreated(false);
            userRepository.save(savedUser);
            emailService.sendWalletPendingReminder(savedUser);
        }

        LoginResponseDto loginResponseDto = checkEmailAndPasswordAndReturnToken(requestDto.getEmail(), requestDto.getPassword(), "registration successful", "failed to register");
        String authHeader = "Bearer " + loginResponseDto.getAccessToken();
        CreateProfileRequest request = new CreateProfileRequest();
        request.setFirstName(request.getFirstName());
        request.setLastName(request.getLastName());
        request.setEmail(request.getEmail());
        request.setSector(request.getSector());
        createProfile.createProfile(authHeader,request);

        return RegisterMapper.toResponse(savedUser, loginResponseDto);
    }


    public LoginResponseDto login (LoginRequestDto loginRequestDto){

        if(loginRequestDto.getEmail() == null || loginRequestDto.getEmail().trim().isEmpty()){
            throw new RuntimeException("Email or Phone Number is Required");
        }

        Optional<User> user = userRepository.findByEmail(loginRequestDto.getEmail());
        if(user.isEmpty()){
            throw new RuntimeException("user not found");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.get().getPassword())){
            throw new RuntimeException("Invalid Login credentials");
        }
      return checkEmailAndPasswordAndReturnToken(loginRequestDto.getEmail(), loginRequestDto.getPassword(), "login successful", "failed to login");
    }


    public LoginResponseDto refreshToken (RefreshTokenRequest refreshTokenRequest){
        return refreshTokenRepository.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::validateToken)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user.getEmail());
                    List<String> roles = jwtService.extractRoles(accessToken);
                    RefreshToken refreshToken = refreshTokenService.createToken(user.getEmail());
                    LoginResponseDto loginResponseDto = new LoginResponseDto();
                    loginResponseDto.setAccessToken(accessToken);
                    loginResponseDto.setRefreshToken(refreshToken.getToken());
                    loginResponseDto.setRoles(roles);
                    loginResponseDto.setMessage("Login successful");
                    return loginResponseDto;
                }).orElseThrow(() -> new RuntimeException("Failed to get refresh Token"));
    }


    public LoginResponseDto checkEmailAndPasswordAndReturnToken (String email, String password, String successMessage, String errorMessage){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                       email,
                        password
                )
        );
        if(authentication.isAuthenticated()){
            String accessToken = jwtService.generateToken(email);
            List<String> roles = jwtService.extractRoles(accessToken);
            RefreshToken refreshToken = refreshTokenService.createToken(email);
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setAccessToken(accessToken);
            loginResponseDto.setRefreshToken(refreshToken.getToken());
            loginResponseDto.setRoles(roles);
            loginResponseDto.setMessage(successMessage);
            return loginResponseDto;

        }else {
            throw new RuntimeException(errorMessage);
        }
    }
}
