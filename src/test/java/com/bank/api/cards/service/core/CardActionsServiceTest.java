package com.bank.api.cards.service.core;

import com.bank.api.cards.entity.Card;
import com.bank.api.cards.entity.User;
import com.bank.api.cards.exception.CardNotFoundException;
import com.bank.api.cards.repository.CardRepository;
import com.bank.api.cards.service.core.impl.CardActionsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardActionsServiceTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardRequestsService cardRequestsService;
    @Mock
    private UserService userService;

    @InjectMocks
    private CardActionsServiceImpl cardActionsService;

    @Test
    void findAndSetExpiredCards_ShouldCallRepository() {
        cardActionsService.findAndSetExpiredCards();
        verify(cardRepository, times(1)).expireCards(LocalDate.now());
    }

    @Test
    void requestCardBlock_ShouldCreateRequest_WhenCardAndUserExist() {
        Long cardId = 1L;
        Long userId = 1L;
        Card card = new Card();
        User user = new User();

        when(cardRepository.findByUserIdAndCardId(userId, cardId)).thenReturn(Optional.of(card));
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        cardActionsService.requestCardBlock(cardId, userId);

        verify(cardRequestsService).addCardBlockRequest(user, card);
    }

    @Test
    void requestCardBlock_ShouldThrowException_WhenCardNotFound() {
        when(cardRepository.findByUserIdAndCardId(anyLong(), anyLong())).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () ->
                cardActionsService.requestCardBlock(1L, 1L)
        );
    }

    @Test
    void updateBalance_ShouldUpdateValue_WhenCardExists() {
        Long cardId = 1L;
        BigDecimal newAmount = new BigDecimal("1000.00");
        Card card = new Card();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        cardActionsService.updateBalance(cardId, newAmount);

        assertEquals(newAmount, card.getBalance());
        verify(cardRepository).save(card);
    }
}
