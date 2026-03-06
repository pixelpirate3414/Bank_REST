package com.bank.api.cards.service.core;

import com.bank.api.cards.entity.CardRequest;

import java.math.BigDecimal;

public interface CardActionsService {

    void findAndSetExpiredCards();

    CardRequest requestCardBlock(Long cardId, Long userId);

    CardRequest requestCardReactivation(Long cardId, Long userId);

    void updateBalance(Long cardId, BigDecimal amount);
}
