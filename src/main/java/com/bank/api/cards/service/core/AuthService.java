package com.bank.api.cards.service.core;

import com.bank.api.cards.entity.User;

public interface AuthService {

    User register(String username, String password);

    String login(String username, String password);
}
