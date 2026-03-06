package com.bank.api.cards.service.facade;

import com.bank.api.cards.dto.UserDto;
import com.bank.api.cards.dto.request.AuthRequest;
import com.bank.api.cards.dto.response.AuthResponse;
import com.bank.api.cards.entity.User;
import com.bank.api.cards.mapper.UserMapper;
import com.bank.api.cards.service.core.AuthService;
import com.bank.api.cards.service.facade.impl.AuthFacadeImpl;
import com.bank.api.cards.util.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthFacadeTest {

    @Mock
    private AuthService authService;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthFacadeImpl authFacade;

    @Test
    void signIn_ShouldReturnAuthResponse_WithToken() {
        AuthRequest request = new AuthRequest("user", "password");
        when(authService.login("user", "password")).thenReturn("fake-jwt-token");

        AuthResponse response = authFacade.signIn(request);

        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getJwt());
        verify(authService).login("user", "password");
    }

    @Test
    void signUp_ShouldReturnUserDto_WhenRegistrationSuccessful() {
        AuthRequest request = new AuthRequest("user", "password");
        User user = new User();
        UserDto userDto = new UserDto(1L, "user", UserRole.USER);

        when(authService.register("user", "password")).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = authFacade.signUp(request);

        assertEquals("user", result.getUserName());
        verify(authService).register("user", "password");
        verify(userMapper).toDto(user);
    }
}
