package com.example.userauth.controller;

import com.example.userauth.dto.RegisterUserDto;
import com.example.userauth.dto.ResendOtpRequest;
import com.example.userauth.dto.VerifyUserDto;
import com.example.userauth.dto.LoginUserDto;
import com.example.userauth.model.OtpToken;
import com.example.userauth.model.User;
import com.example.userauth.responses.LoginResponse;
import com.example.userauth.service.AuthenticationService;
import com.example.userauth.service.JwtService;
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
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto login ){
        User authenticatedUser = authenticationService.authenticate(login);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        boolean otpRequired  = true;
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime(), otpRequired);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> VerifyUser(@RequestBody VerifyUserDto verify){
            try{
                authenticationService.verifyUser(verify);
                return ResponseEntity.ok("Account verified successfully");
            }catch (RuntimeException e){
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
