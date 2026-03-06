package com.bank.api.cards.service.facade;

import com.bank.api.cards.dto.CardDto;
import com.bank.api.cards.dto.UserDto;
import com.bank.api.cards.dto.request.TransferRequest;
import com.bank.api.cards.dto.response.CardBalanceResponse;
import com.bank.api.cards.dto.response.CardStatusResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserFacade {

    CardDto getUserCard(Long userId, Long cardId);

    Page<CardDto> getUserCards(Long userId, Pageable pageable);

    CardStatusResponse requestCardBlock(Long userId, Long cardId);

    CardBalanceResponse transferMoney(TransferRequest request);

    CardStatusResponse requestCardReactivation(Long userId, Long cardId);
}
