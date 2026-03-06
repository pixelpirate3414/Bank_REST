package com.bank.api.cards.service.core;

import com.bank.api.cards.entity.Card;
import com.bank.api.cards.entity.CardRequest;
import com.bank.api.cards.util.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface CardService {

    Optional<Card> getCard(Long cardId);

    Page<Card> getCards(Pageable pageable);

    Optional<Card> getUserCard(Long cardId, Long userId);

    Page<Card> getUserCards(Long userId, Pageable pageable);

    Card createCard(Long userId);

    Card updateStatus(Long cardId, CardStatus newStatus);

    void deleteCard(Long cardId);
}
