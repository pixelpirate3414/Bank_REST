package com.bank.api.cards.service.core;

import com.bank.api.cards.entity.User;
import com.bank.api.cards.security.JwtUtil;
import com.bank.api.cards.service.core.impl.AuthServiceImpl;
import com.bank.api.cards.util.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private SecurityUtils securityUtils;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthServiceImpl authService;

    private final String username = "testUser";
    private final String password = "password123";

    @Test
    void register_ShouldSaveUser_WhenUserDoesNotExist() {
        when(userService.userExists(username)).thenReturn(false);
        when(securityUtils.encodePassword(password)).thenReturn("encodedPassword");

        authService.register(username, password);

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void register_ShouldThrowException_WhenUserAlreadyExists() {
        when(userService.userExists(username)).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                authService.register(username, password)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("fake-jwt-token");

        String token = authService.login(username, password);

        assertNotNull(token);
        assertEquals("fake-jwt-token", token);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
