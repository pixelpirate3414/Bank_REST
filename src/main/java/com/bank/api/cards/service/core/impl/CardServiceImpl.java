package com.bank.api.cards.service.core.impl;

import com.bank.api.cards.entity.Card;
import com.bank.api.cards.entity.CardRequest;
import com.bank.api.cards.entity.User;
import com.bank.api.cards.exception.CardNotFoundException;
import com.bank.api.cards.exception.UserNotFoundException;
import com.bank.api.cards.repository.CardRepository;
import com.bank.api.cards.service.core.CardRequestsService;
import com.bank.api.cards.service.core.CardService;
import com.bank.api.cards.service.core.UserService;
import com.bank.api.cards.util.CardStatus;
import com.bank.api.cards.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Сервис для управления операциями над картами.
 * Обеспечивает бизнес-логику создания, получения и изменения статуса карт.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserService userService;
    private final SecurityUtils securityUtils;

    @Override
    public Optional<Card> getCard(Long cardId) {
        return cardRepository.findById(cardId);
    }

    @Override
    public Page<Card> getCards(Pageable pageable) {
        return cardRepository.findAll(pageable);
    }

    @Override
    public Optional<Card> getUserCard(Long cardId, Long userId) {
        return cardRepository.findByUserIdAndCardId(userId, cardId);
    }

    @Override
    public Page<Card> getUserCards(Long userId, Pageable pageable) {
        return cardRepository.findAllByUserId(userId, pageable);
    }

    /**
     * Создает новую карту для пользователя с механизмом повторных попыток
     * при возникновении конфликта уникальности номера.
     */
    @Override
    @Transactional
    public Card createCard(Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return createCardWithRetry(user, 10);
    }

    private Card createCardWithRetry(User user, int attempts) {
        if (attempts <= 0) {
            throw new IllegalStateException("Failed to generate unique card number");
        }

        String rawNumber = generateCardNumber();
        String encodedNumber = securityUtils.encodeCardNumber(rawNumber);
        String maskedNumber = securityUtils.maskCard(rawNumber);

        Card card = Card.builder()
                .number(encodedNumber)
                .maskedNumber(maskedNumber)
                .expiredAt(LocalDate.now().plusYears(10))
                .balance(BigDecimal.ZERO)
                .status(CardStatus.ACTIVE)
                .user(user)
                .build();

        try {
            return cardRepository.save(card);
        } catch (DataIntegrityViolationException e) {
            return createCardWithRetry(user, attempts - 1);
        }
    }

    private String generateCardNumber() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append((int) (Math.random() * 10));
            if ((i + 1) % 4 == 0 && i != 15) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    @Override
    @Transactional
    public Card updateStatus(Long cardId, CardStatus newStatus) {
        Card cardToUpdate = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        cardToUpdate.setStatus(newStatus);
        return cardToUpdate;
    }

    @Override
    @Transactional
    public void deleteCard(Long cardId) {
        cardRepository.deleteById(cardId);
    }
}
