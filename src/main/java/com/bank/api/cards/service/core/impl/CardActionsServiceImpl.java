package com.bank.api.cards.service.core.impl;

import com.bank.api.cards.entity.Card;
import com.bank.api.cards.entity.CardRequest;
import com.bank.api.cards.entity.User;
import com.bank.api.cards.exception.CardNotFoundException;
import com.bank.api.cards.exception.UserNotFoundException;
import com.bank.api.cards.repository.CardRepository;
import com.bank.api.cards.service.core.CardActionsService;
import com.bank.api.cards.service.core.CardRequestsService;
import com.bank.api.cards.service.core.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Сервис для выполнения специфических действий над картами,
 * таких как массовая проверка срока действия, создание заявок и обновление баланса.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardActionsServiceImpl implements CardActionsService {

    private final CardRepository cardRepository;
    private final CardRequestsService cardRequestsService;
    private final UserService userService;

    /**
     * Выполняет массовую проверку карт на истечение срока действия
     * и обновление их статуса в БД.
     */
    @Override
    @Transactional
    public void findAndSetExpiredCards() {
        cardRepository.expireCards(LocalDate.now());
    }

    /**
     * Создает запрос на блокировку карты.
     * @param cardId ID карты.
     * @param userId ID владельца.
     * @return созданный запрос на блокировку.
     */
    @Override
    @Transactional
    public CardRequest requestCardBlock(Long cardId, Long userId) {
        Card card = cardRepository.findByUserIdAndCardId(userId, cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return cardRequestsService.addCardBlockRequest(user, card);
    }

    /**
     * Создает запрос на разблокировку карты.
     * @param cardId ID карты.
     * @param userId ID владельца.
     * @return созданный запрос на разблокировку.
     */
    @Override
    @Transactional
    public CardRequest requestCardReactivation(Long cardId, Long userId) {
        Card card = cardRepository.findByUserIdAndCardId(userId, cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return cardRequestsService.addCardReactivationRequest(user, card);
    }

    /**
     * Обновляет баланс карты.
     * @param cardId ID карты.
     * @param amount новая сумма баланса.
     */
    @Override
    @Transactional
    public void updateBalance(Long cardId, BigDecimal amount) {
        Card cardToUpdate = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        cardToUpdate.setBalance(amount);

        cardRepository.save(cardToUpdate);
    }
}
