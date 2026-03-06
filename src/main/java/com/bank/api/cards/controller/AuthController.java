package com.bank.api.cards.controller;

import com.bank.api.cards.dto.UserDto;
import com.bank.api.cards.dto.request.AuthRequest;
import com.bank.api.cards.dto.response.AuthResponse;
import com.bank.api.cards.service.facade.AuthFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${app.api.version}/auth")
public class AuthController {
    
    private final AuthFacade authFacade;

    @PostMapping("/sign-up")
    public UserDto singUp(@RequestBody @Valid AuthRequest req) {
        return authFacade.signUp(req);
    }

    @PostMapping("/sign-in")
    public AuthResponse singIn(@RequestBody @Valid AuthRequest req) {
        return authFacade.signIn(req);
    }
}
