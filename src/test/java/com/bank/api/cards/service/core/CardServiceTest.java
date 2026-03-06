package com.bank.api.cards.service.core;

import com.bank.api.cards.entity.Card;
import com.bank.api.cards.entity.User;
import com.bank.api.cards.repository.CardRepository;
import com.bank.api.cards.service.core.impl.CardServiceImpl;
import com.bank.api.cards.util.CardStatus;
import com.bank.api.cards.util.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserService userService;
    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    void createCard_ShouldRetryOnDataIntegrityViolation() {
        Long userId = 1L;
        User user = new User();

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        when(cardRepository.save(any(Card.class)))
                .thenThrow(DataIntegrityViolationException.class)
                .thenReturn(new Card());

        Card result = cardService.createCard(userId);

        assertNotNull(result);
        verify(cardRepository, times(2)).save(any(Card.class));
    }

    @Test
    void createCard_ShouldThrowException_WhenAttemptsExhausted() {
        Long userId = 1L;
        User user = new User();

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));
        when(cardRepository.save(any(Card.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(IllegalStateException.class, () -> cardService.createCard(userId));
    }

    @Test
    void updateStatus_ShouldUpdateAndReturnCard() {
        Long cardId = 1L;
        Card card = new Card();
        card.setStatus(CardStatus.ACTIVE);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        Card updatedCard = cardService.updateStatus(cardId, CardStatus.BLOCKED);

        assertEquals(CardStatus.BLOCKED, updatedCard.getStatus());
    }
}
