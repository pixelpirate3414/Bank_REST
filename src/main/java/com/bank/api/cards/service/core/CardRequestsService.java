package com.bank.api.cards.service.core;

import com.bank.api.cards.entity.Card;
import com.bank.api.cards.entity.CardRequest;
import com.bank.api.cards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CardRequestsService {

    Optional<CardRequest> getCardBlockRequest(Long requestId);

    Page<CardRequest> getCardBlockRequests(Pageable pageable);

    CardRequest addCardBlockRequest(User user, Card card);

    CardRequest addCardReactivationRequest(User user, Card card);

    void deleteCardBlockRequest(Long requestId);

    CardRequest updateCardRequest(CardRequest cardRequest);
}
