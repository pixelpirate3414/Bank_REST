package com.bank.api.cards.service.core.impl;

import com.bank.api.cards.entity.User;
import com.bank.api.cards.repository.UserRepository;
import com.bank.api.cards.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Реализация сервиса для управления пользователями.
 * Отвечает за взаимодействие с репозиторием и базовые операции CRUD.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Создает нового пользователя в системе.
     * @param user сущность пользователя.
     * @return сохраненная сущность пользователя.
     */
    @Override
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Ищет пользователя по его ID.
     * @param id ID пользователя.
     * @return Optional с найденным пользователем.
     */
    @Override
    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    /**
     * Ищет пользователя по его имени пользователя (username).
     * @param username имя пользователя.
     * @return Optional с найденным пользователем.
     */
    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Удаляет пользователя из системы по ID.
     * @param id ID пользователя.
     */
    @Override
    @Transactional // Обязательно для операции удаления
    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }

    /**
     * Получает список всех пользователей с пагинацией.
     * @param pageable параметры пагинации.
     * @return страница пользователей.
     */
    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Проверяет наличие пользователя с заданным именем.
     * @param username имя пользователя.
     * @return true, если пользователь существует, иначе false.
     */
    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
