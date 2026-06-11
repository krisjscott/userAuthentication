package com.example.userauth.service;

import com.example.userauth.dto.RegisterUserDto;
import com.example.userauth.dto.VerifyUserDto;
import com.example.userauth.dto.LoginUserDto;
import com.example.userauth.model.User;
import com.example.userauth.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationService(
            UserRepository userRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            AuthenticationManager authenticationManager,
            EmailService emailService
    ){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    public User signUp(RegisterUserDto input){
        User user = new User(input.getUsername(),input.getMail(), bCryptPasswordEncoder.encode(input.getPassword()));
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationExpiration(LocalDateTime.now().plusMinutes(10));
        user.setEnabled(false);
        sendVerificationEmail(user);

        return userRepository.save(user);
    }
//    public User authenticate(LoginUserDto input){
//        User user = userRepository.findByEmail(input.getEmail())
//                .orElseThrow(() -> new RuntimeException("User doesn't exist"));
//
//        if(!user.isEnabled()){
//            throw new RuntimeException("Account is not enabled");
//        }
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        input.getEmail(),
//                        input.getPassword()
//                )
//        );
//        return user;
//    }

    public void initiateLogin(LoginUserDto input) throws MessagingException {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("User doesn't exist"));


        if(!user.isEnabled()) {
            throw new RuntimeException("User is disabled");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        OtpService otpService = null;
        otpService.generateAndSendOtp(input.getEmail());

    }
    public void verifyUser(VerifyUserDto input){
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(user.getVerificationExpiration().isBefore(LocalDateTime.now())){
                throw new RuntimeException("User verification time is expired");
            }
            if(user.getVerificationCode().equals(input.getVerificationCode())){
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationExpiration(null);
                userRepository.save(user);
            } else{
                throw new RuntimeException("Verification code is incorrect");
            }
        } else{
            throw new RuntimeException("User doesn't exist");
        }
    }

    public void resendVerificationCode(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(user.isEnabled()){
                throw new RuntimeException("Account is already enabled");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationExpiration(LocalDateTime.now().plusMinutes(30));
            sendVerificationEmail(user);
            userRepository.save(user);
        }
        else{
            throw new RuntimeException("User doesn't exist");
        }
    }
    public void sendVerificationEmail(User user){
        String subject = "Account verification";
        String verificationCode = "Verification code" + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        try{
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        }
        catch(MessagingException e){
            e.printStackTrace();
        }
    }
    private String generateVerificationCode(){
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

}
