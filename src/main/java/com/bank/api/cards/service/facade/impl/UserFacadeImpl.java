package com.bank.api.cards.service.facade.impl;

import com.bank.api.cards.dto.CardDto;
import com.bank.api.cards.dto.UserDto;
import com.bank.api.cards.dto.request.TransferRequest;
import com.bank.api.cards.dto.response.CardBalanceResponse;
import com.bank.api.cards.dto.response.CardStatusResponse;
import com.bank.api.cards.entity.Card;
import com.bank.api.cards.entity.CardRequest;
import com.bank.api.cards.exception.CardNotFoundException;
import com.bank.api.cards.exception.UserNotFoundException;
import com.bank.api.cards.mapper.CardMapper;
import com.bank.api.cards.mapper.UserMapper;
import com.bank.api.cards.service.core.CardActionsService;
import com.bank.api.cards.service.core.CardService;
import com.bank.api.cards.service.core.TransactionService;
import com.bank.api.cards.service.core.UserService;
import com.bank.api.cards.service.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Фасад для управления операциями пользователей и их карт.
 * Обеспечивает транзакционную целостность бизнес-процессов, объединяя логику
 * различных сервисов (CardService, TransactionService, UserService).
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserFacadeImpl implements UserFacade {

    private final CardService cardService;
    private final CardMapper cardMapper;
    private final TransactionService transactionService;
    private final CardActionsService cardActionsService;

    /**
     * Получает информацию о конкретной карте пользователя.
     * @param userId ID пользователя.
     * @param cardId ID карты.
     * @return DTO карты.
     * @throws CardNotFoundException если карта не найдена.
     */
    @Override
    public CardDto getUserCard(Long userId, Long cardId) {
        return cardService.getUserCard(userId, cardId)
                .map(cardMapper::toDto)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
    }

    /**
     * Получает список карт пользователя с поддержкой пагинации.
     * @param userId ID пользователя.
     * @param pageable параметры страницы.
     * @return страница DTO карт.
     */
    @Override
    public Page<CardDto> getUserCards(Long userId, Pageable pageable) {
        return cardService.getUserCards(userId, pageable)
                .map(cardMapper::toDto);
    }

    /**
     * Инициирует процесс блокировки карты.
     * @param userId ID пользователя.
     * @param cardId ID карты.
     * @return статус блокировки.
     */
    @Override
    @Transactional
    public CardStatusResponse requestCardBlock(Long userId, Long cardId) {
        CardRequest request = cardActionsService.requestCardBlock(userId, cardId);

        return new CardStatusResponse(
                request.getCard().getMaskedNumber(),
                request.getCard().getStatus()
        );
    }

    /**
     * Выполняет перевод денежных средств между картами.
     * Метод помечен как @Transactional, чтобы гарантировать, что списание и
     * зачисление пройдут успешно или будут полностью отменены при ошибке.
     * * @param request объект с данными перевода.
     * @return баланс карты после транзакции.
     */
    @Override
    @Transactional
    public CardBalanceResponse transferMoney(TransferRequest request) {
        transactionService.transfer(request.getFromCardId(), request.getToCardId(), request.getAmount());

        Card card = cardService.getUserCard(request.getFromUserId(), request.getFromCardId())
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        return new CardBalanceResponse(
                card.getMaskedNumber(),
                card.getBalance()
        );
    }

    /**
     * Инициирует процесс реактивации карты.
     * @param userId ID пользователя.
     * @param cardId ID карты.
     * @return статус блокировки.
     */
    @Override
    @Transactional
    public CardStatusResponse requestCardReactivation(Long userId, Long cardId) {
        CardRequest request = cardActionsService.requestCardReactivation(userId, cardId);

        return new CardStatusResponse(
                request.getCard().getMaskedNumber(),
                request.getCard().getStatus()
        );
    }
}
