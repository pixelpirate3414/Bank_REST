package com.bank.api.cards.service.facade.impl;

import com.bank.api.cards.dto.UserDto;
import com.bank.api.cards.dto.request.AuthRequest;
import com.bank.api.cards.dto.response.AuthResponse;
import com.bank.api.cards.entity.User;
import com.bank.api.cards.mapper.UserMapper;
import com.bank.api.cards.service.core.AuthService;
import com.bank.api.cards.service.facade.AuthFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Фасад для управления процессом аутентификации и регистрации пользователей.
 * Координирует взаимодействие между сервисом авторизации и мапперами данных.
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthFacadeImpl implements AuthFacade {

    private final AuthService authService;
    private final UserMapper userMapper;

    /**
     * Выполняет вход пользователя в систему.
     * @param request данные для входа (логин/пароль).
     * @return ответ с токеном доступа.
     */
    @Override
    public AuthResponse signIn(AuthRequest request) {
        String token = authService.login(request.getUserName(), request.getPassword());
        return new AuthResponse(token);
    }

    /**
     * Регистрирует нового пользователя в системе.
     * Метод помечен как @Transactional, так как операция подразумевает создание новой записи в БД.
     * @param request данные для регистрации.
     * @return DTO созданного пользователя.
     */
    @Override
    @Transactional // Обязательно для записи в БД
    public UserDto signUp(AuthRequest request) {
        User user = authService.register(request.getUserName(), request.getPassword());
        return userMapper.toDto(user);
    }
}
