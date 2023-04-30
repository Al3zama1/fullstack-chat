package com.abranlezama.fullstackchatapplication.service.imp;

import com.abranlezama.fullstackchatapplication.dto.authentication.AuthRequest;
import com.abranlezama.fullstackchatapplication.dto.authentication.TokenResponse;
import com.abranlezama.fullstackchatapplication.exception.AuthenticationException;
import com.abranlezama.fullstackchatapplication.exception.ExceptionMessages;
import com.abranlezama.fullstackchatapplication.exception.UsernameTakenException;
import com.abranlezama.fullstackchatapplication.model.User;
import com.abranlezama.fullstackchatapplication.repository.UserRepository;
import com.abranlezama.fullstackchatapplication.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(SpringExtension.class)
class AuthenticationServiceImpTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthenticationServiceImp cut;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void shouldRegisterUser() {
        // Given
        AuthRequest authRequest = new AuthRequest("Duke", "12345678");
        User user = User.builder().username("Duke").password("12345678").lastUpdated(LocalDateTime.now())
                        .build();

        given(userRepository.findByUsername(authRequest.username())).willReturn(Optional.empty());

        // When
        cut.registerUser(authRequest);

        // Then
        then(userRepository).should().save(userArgumentCaptor.capture());
        assertThat(userArgumentCaptor.getValue().getUsername()).isEqualTo(authRequest.username());
    }

    @Test
    void shouldThrowUsernameTakenExceptionWhenUsernameIsTaken() {
        // Given
        AuthRequest authRequest = new AuthRequest("Duke", "12345678");

        given(userRepository.findByUsername(authRequest.username())).willReturn(Optional.of(new User()));

        // When
        assertThatThrownBy(() -> cut.registerUser(authRequest))
                .hasMessage(ExceptionMessages.USERNAME_TAKEN)
                .isInstanceOf(UsernameTakenException.class);

        // Then
        then(userRepository).should(never()).save(any());
    }

    @Test
    void shouldAuthenticateUser() {
        // Given
        AuthRequest authRequest = new AuthRequest("Duke", "12345678");
        User user = new User("fsf", authRequest.username(), authRequest.password(), LocalDateTime.now());

        given(userRepository.findByUsername(authRequest.username())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(authRequest.password(), user.getPassword())).willReturn(true);
        given(jwtService.generateToken(user)).willReturn("token");

        // When
        TokenResponse token = cut.login(authRequest);

        // Then
        assertThat(token.jwtToken()).isEqualTo("token");
    }

    @Test
    void shouldThrowAuthenticationExceptionWhenUsernameDoesNotMatch() {
        // Given
        AuthRequest authRequest = new AuthRequest("Duke", "12345678");

        given(userRepository.findByUsername(authRequest.username())).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() -> cut.login(authRequest))
                .hasMessage(ExceptionMessages.WRONG_CREDENTIALS)
                .isInstanceOf(AuthenticationException.class);

        // Then
        then(jwtService).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowAuthenticationExceptionWhenPasswordDoesNotMatch() {
        // Given
        AuthRequest authRequest = new AuthRequest("Duke", "12345678");
        User user = new User("fsf", authRequest.username(), authRequest.password(), LocalDateTime.now());

        given(userRepository.findByUsername(authRequest.username())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(authRequest.password(), user.getPassword())).willReturn(false);

        // When
        assertThatThrownBy(() -> cut.login(authRequest))
                .hasMessage(ExceptionMessages.WRONG_CREDENTIALS)
                .isInstanceOf(AuthenticationException.class);

        // Then
        then(jwtService).shouldHaveNoInteractions();
    }

}
