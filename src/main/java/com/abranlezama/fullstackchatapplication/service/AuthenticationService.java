package com.abranlezama.fullstackchatapplication.service;

import com.abranlezama.fullstackchatapplication.dto.authentication.AuthRequest;
import com.abranlezama.fullstackchatapplication.dto.authentication.TokenResponse;

public interface AuthenticationService {

    TokenResponse registerUser(AuthRequest authRequest);

    TokenResponse login(AuthRequest authRequest);
}
