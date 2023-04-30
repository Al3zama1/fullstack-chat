package com.abranlezama.fullstackchatapplication.controller;

import com.abranlezama.fullstackchatapplication.config.SecurityConfig;
import com.abranlezama.fullstackchatapplication.dto.authentication.AuthRequest;
import com.abranlezama.fullstackchatapplication.service.AuthenticationService;
import com.abranlezama.fullstackchatapplication.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@Import(value = {SecurityConfig.class})
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserDetailsService userDetailsService;



//    private static final
    private static final  Faker FAKER = new Faker();

    @Test
    void shouldCallAuthenticationServiceToRegisterUser() throws Exception {
        // Given
        AuthRequest authRequest = new AuthRequest(FAKER.name().username(), "123455");

        // When
        mockMvc.perform(post("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isCreated());

        // Then
        then(authenticationService).should().registerUser(authRequest);
    }

    @Test
    void shouldCallAuthenticationServiceToAuthenticateUser() throws Exception {
        // Given
        AuthRequest authRequest = new AuthRequest(FAKER.name().username(), "123455");

        // When
        mockMvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk());

        // Then
        then(authenticationService).should().login(authRequest);

    }

}
