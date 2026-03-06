package com.bank.api.cards.service.facade.impl;

import com.bank.api.cards.dto.CardAdminDto;
import com.bank.api.cards.dto.CardRequestDto;
import com.bank.api.cards.dto.UserAdminDto;
import com.bank.api.cards.entity.Card;
import com.bank.api.cards.entity.CardRequest;
import com.bank.api.cards.exception.CardNotFoundException;
import com.bank.api.cards.exception.CardRequestNotFoundException;
import com.bank.api.cards.exception.UserNotFoundException;
import com.bank.api.cards.mapper.CardMapper;
import com.bank.api.cards.mapper.CardRequestsMapper;
import com.bank.api.cards.mapper.UserMapper;
import com.bank.api.cards.service.core.CardRequestsService;
import com.bank.api.cards.service.core.CardService;
import com.bank.api.cards.service.core.UserService;
import com.bank.api.cards.service.facade.AdminFacade;
import com.bank.api.cards.util.CardRequestStatus;
import com.bank.api.cards.util.CardStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Фасад для управления административными функциями системы.
 * Обеспечивает централизованный доступ к управлению пользователями и картами,
 * гарантируя целостность данных при выполнении административных действий.
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true) // По умолчанию все методы - только чтение
public class AdminFacadeImpl implements AdminFacade {

    private final CardService cardService;
    private final UserService userService;
    private final CardRequestsService cardRequestsService;
    private final CardMapper cardMapper;
    private final UserMapper userMapper;
    private final CardRequestsMapper cardRequestsMapper;

    @Override
    public CardAdminDto getCard(Long cardId) {
        return cardService.getCard(cardId)
                .map(cardMapper::toAdminDto)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
    }

    @Override
    public Page<CardAdminDto> getAllCards(int page, int size) {
        return cardService.getCards(PageRequest.of(page, size))
                .map(cardMapper::toAdminDto);
    }

    @Override
    public Page<CardAdminDto> getAllUserCards(Long userId, int page, int size) {
        return cardService.getUserCards(userId, PageRequest.of(page, size))
                .map(cardMapper::toAdminDto);
    }

    @Override
    @Transactional
    public CardAdminDto createCard(Long userId) {
        return cardMapper.toAdminDto(cardService.createCard(userId));
    }

    @Override
    @Transactional
    public CardAdminDto blockCard(Long requestId) {
        CardRequest request = cardRequestsService.getCardBlockRequest(requestId)
                .orElseThrow(() -> new CardRequestNotFoundException("Request not found"));

        Card card = cardService.updateStatus(request.getCard().getId(), CardStatus.BLOCKED);

        request.setStatus(CardRequestStatus.PROCESSED);
        cardRequestsService.updateCardRequest(request);

        return cardMapper.toAdminDto(card);
    }

    @Override
    @Transactional
    public CardAdminDto activateCard(Long cardId) {
        return cardMapper.toAdminDto(cardService.updateStatus(cardId, CardStatus.ACTIVE));
    }

    @Override
    @Transactional
    public void deleteCard(Long cardId) {
        cardService.deleteCard(cardId);
    }

    @Override
    public UserAdminDto getUser(Long userId) {
        return userService.getUserById(userId)
                .map(userMapper::toAdminDto)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public Page<UserAdminDto> getUsers(int page, int size) {
        return userService.getAllUsers(PageRequest.of(page, size))
                .map(userMapper::toAdminDto);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userService.deleteUserById(userId);
    }

    @Override
    public CardRequestDto getBlockRequest(Long requestId) {
        return cardRequestsService.getCardBlockRequest(requestId)
                .map(cardRequestsMapper::toDto)
                .orElseThrow(() -> new CardRequestNotFoundException("Request not found"));
    }

    @Override
    public Page<CardRequestDto> getAllBlockRequests(int page, int size) {
        return cardRequestsService.getCardBlockRequests(PageRequest.of(page, size))
                .map(cardRequestsMapper::toDto);
    }
}
