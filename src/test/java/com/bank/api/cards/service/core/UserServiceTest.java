package com.bank.api.cards.service.core;

import com.bank.api.cards.entity.User;
import com.bank.api.cards.repository.UserRepository;
import com.bank.api.cards.service.core.impl.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_ShouldSaveAndReturnUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User created = userService.createUser(user);

        assertNotNull(created);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUserById_ShouldReturnOptionalUser() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        Optional<User> user = userService.getUserById(userId);

        assertTrue(user.isPresent());
        verify(userRepository).findById(userId);
    }

    @Test
    void userExists_ShouldReturnTrue_WhenUserExists() {
        String username = "testUser";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        boolean exists = userService.userExists(username);

        assertTrue(exists);
        verify(userRepository).existsByUsername(username);
    }

    @Test
    void deleteUserById_ShouldCallRepositoryDelete() {
        long userId = 1L;
        userService.deleteUserById(userId);
        verify(userRepository).deleteById(userId);
    }
}
