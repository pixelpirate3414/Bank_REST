package com.bank.api.cards.service.facade;

import com.bank.api.cards.dto.UserDto;
import com.bank.api.cards.dto.request.AuthRequest;
import com.bank.api.cards.dto.response.AuthResponse;

public interface AuthFacade {

    AuthResponse signIn(AuthRequest request);

    UserDto signUp(AuthRequest request);
}
