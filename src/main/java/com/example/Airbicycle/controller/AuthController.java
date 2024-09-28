package com.example.Airbicycle.controller;

import com.example.Airbicycle.dto.JwtRequest;
import com.example.Airbicycle.dto.JwtResponse;
import com.example.Airbicycle.dto.SigninRequest;
import com.example.Airbicycle.dto.SignupRequest;
import com.example.Airbicycle.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<JwtResponse> signUp(@RequestBody SignupRequest signUpRequest){
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> signIn(@RequestBody SigninRequest signInRequest){
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody JwtRequest refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }
    @PostMapping("/validate")
    public ResponseEntity<JwtResponse> validateToken(@RequestBody JwtRequest validateTokenRequest){
        return ResponseEntity.ok(authService.validateToken(validateTokenRequest));
    }


}
