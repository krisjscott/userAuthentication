package com.example.userauth.service;

import com.example.userauth.model.OtpToken;
import com.example.userauth.model.User;
import com.example.userauth.repository.OtpTokenRepository;
import com.example.userauth.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpService {
    private final OtpTokenRepository otpTokenRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
//    private final OtpToken otpToken;
//    private final BCryptPasswordEncoder  bCryptPasswordEncoder;

    public OtpService(OtpTokenRepository otpTokenRepository, EmailService emailService,
                      UserRepository userRepository) {
        this.otpTokenRepository = otpTokenRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @Transactional
    public void  generateAndSendOtp(String email) throws MessagingException {
        String Subject = "OTP";
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        otpTokenRepository.deleteByUser(user);

        String code = String.format("%06d", new SecureRandom().nextInt(1000000));

        OtpToken otpToken = OtpToken.builder()
                .user(user)
                .code(code)
                .expireTime(LocalDateTime.now().plusMinutes(10))
                .build();

        otpTokenRepository.save(otpToken);

        emailService.sendVerificationEmail(user.getEmail(), Subject, code);
    }

    @Transactional
    public User verifyOtp(String email, String otp) throws MessagingException {

        OtpToken otpToken = otpTokenRepository.findFirstByUserEmailOrderByCreateTimeDesc(email);

        if(otpToken == null) {
            throw new RuntimeException("No OTP found for this email");
        }
        if(otpToken.isExpired()) {
            throw new RuntimeException("Invalid OTP");
        }
        if(!otpToken.getCode().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        otpTokenRepository.delete(otpToken);
        return otpToken.getUser();
    }
}
