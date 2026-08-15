package com.hacthon.ai_rti_assistant.service.impl;

import com.hacthon.ai_rti_assistant.dto.Request.LoginRequest;
import com.hacthon.ai_rti_assistant.dto.Request.RegisterRequest;
import com.hacthon.ai_rti_assistant.dto.Response.LoginResponse;
import com.hacthon.ai_rti_assistant.entity.User;
import com.hacthon.ai_rti_assistant.exception.BadRequestException;
import com.hacthon.ai_rti_assistant.exception.EmailallReady;
import com.hacthon.ai_rti_assistant.repository.UserRepository;
import com.hacthon.ai_rti_assistant.service.AuthService;
import com.hacthon.ai_rti_assistant.service.JwtService;
import com.hacthon.ai_rti_assistant.service.OtpService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            OtpService otpService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.otpService = otpService;
    }

    @Override
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailallReady("Email already registered");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Never store plain-text passwords
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);

        // Send OTP to registered email
        otpService.sendOtp(user.getEmail());
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new BadRequestException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(token);
    }

    @Override
    public void verifyOtp(String email, String otp) {

        boolean verified = otpService.verifyOtp(email, otp);

        if (!verified) {
            throw new BadRequestException("OTP verification failed");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BadRequestException("User not found"));

        user.setEmailVerified(true);

        userRepository.save(user);
    }

    @Override
    public void resendOtp(String email) {

        if (!userRepository.existsByEmail(email)) {
            throw new BadRequestException("User not found");
        }

        otpService.resendOtp(email);
    }
}