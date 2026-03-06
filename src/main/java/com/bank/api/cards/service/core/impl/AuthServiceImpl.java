package com.bank.api.cards.service.core.impl;

import com.bank.api.cards.entity.User;
import com.bank.api.cards.security.JwtUtil;
import com.bank.api.cards.service.core.AuthService;
import com.bank.api.cards.service.core.UserService;
import com.bank.api.cards.util.SecurityUtils;
import com.bank.api.cards.util.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Сервис для обработки логики аутентификации и регистрации пользователей.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final SecurityUtils securityUtils;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * Регистрирует нового пользователя в системе.
     * @param password пароль (будет закодирован перед сохранением).
     * @return созданная сущность пользователя.
     * @throws ResponseStatusException если пользователь уже существует.
     */
    @Override
    @Transactional
    public User register(String username, String password) {
        if (userService.userExists(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }

        String encodedPassword = securityUtils.encodePassword(password);

        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .role(UserRole.USER)
                .build();

        return userService.createUser(user);
    }

    /**
     * Выполняет аутентификацию и генерирует JWT токен.
     * * @param username имя пользователя.
     * @param password пароль.
     * @return JWT токен.
     */
    @Override
    public String login(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtUtil.generateToken(userDetails);
    }
}
