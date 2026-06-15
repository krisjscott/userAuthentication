package com.example.userauth.controller;

import com.example.userauth.dto.*;
import com.example.userauth.model.User;
import com.example.userauth.responses.LoginResponse;
import com.example.userauth.service.AuthenticationService;
import com.example.userauth.service.JwtService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;


    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;

    }
    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody RegisterUserDto registerUserDto){
        User registeredUser = authenticationService.signUp(registerUserDto);
        String s = registeredUser.toString();
        return ResponseEntity.ok(s);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto login ) throws MessagingException {
        User authenticatedUser = authenticationService.initiateLogin(login);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        boolean otpRequired  = true;
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime(), otpRequired);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> VerifyOTP(@RequestBody OtpVerifyDto dto) throws MessagingException {
        User user = authenticationService.completeOtpLogin(dto);
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok((new LoginResponse(token, jwtService.getExpirationTime(), false)));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto){
        try {
            authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestBody ResendOtpRequest request){
        try{
            authenticationService.resendVerificationCode(request.getEmail());
            return ResponseEntity.ok("Account resend successfully");
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
