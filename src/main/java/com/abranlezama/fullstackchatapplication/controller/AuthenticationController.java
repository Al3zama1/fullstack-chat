package com.abranlezama.fullstackchatapplication.controller;

import com.abranlezama.fullstackchatapplication.dto.authentication.AuthRequest;
import com.abranlezama.fullstackchatapplication.dto.authentication.TokenResponse;
import com.abranlezama.fullstackchatapplication.service.AuthenticationService;
import com.abranlezama.fullstackchatapplication.service.imp.AuthenticationServiceImp;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse register(@Valid @RequestBody AuthRequest authRequest,
                        HttpServletResponse response) {
        return authenticationService.registerUser(authRequest);
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody AuthRequest authRequest) {
        return authenticationService.login(authRequest);
    }
}
