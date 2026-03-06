package com.bank.api.cards.controller;

import com.bank.api.cards.dto.request.TransferRequest;
import com.bank.api.cards.dto.response.CardBalanceResponse;
import com.bank.api.cards.service.facade.UserFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.api.version}/transactions")
@RequiredArgsConstructor
public class TransactionController {
    
    private final UserFacade userFacade;

    @PostMapping("/transfer")
    public ResponseEntity<CardBalanceResponse> processTransfer(@RequestBody @Valid TransferRequest transferRequest) {
        return ResponseEntity.ok(
            userFacade.transferMoney(transferRequest)
        );
    }
    
}
