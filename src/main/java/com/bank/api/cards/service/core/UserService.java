package com.bank.api.cards.service.core;

import com.bank.api.cards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {

    User createUser(User user);

    Optional<User> getUserById(long id);

    Optional<User> getUserByUsername(String username);

    void deleteUserById(long id);

    Page<User> getAllUsers(Pageable pageable);

    boolean userExists(String username);
}
