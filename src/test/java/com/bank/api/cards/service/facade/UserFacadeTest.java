package com.bank.api.cards.service.facade;

import com.bank.api.cards.dto.CardDto;
import com.bank.api.cards.dto.request.TransferRequest;
import com.bank.api.cards.dto.response.CardBalanceResponse;
import com.bank.api.cards.dto.response.CardStatusResponse;
import com.bank.api.cards.entity.Card;
import com.bank.api.cards.entity.CardRequest;
import com.bank.api.cards.mapper.CardMapper;
import com.bank.api.cards.service.core.CardActionsService;
import com.bank.api.cards.service.core.CardService;
import com.bank.api.cards.service.core.TransactionService;
import com.bank.api.cards.service.facade.impl.UserFacadeImpl;
import com.bank.api.cards.util.CardStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {

    @Mock
    private CardService cardService;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private TransactionService transactionService;
    @Mock
    private CardActionsService cardActionsService;

    @InjectMocks
    private UserFacadeImpl userFacade;

    @Test
    void getUserCard_ShouldMapToDto_WhenCardExists() {
        Long userId = 1L;
        Long cardId = 1L;
        Card card = new Card();
        CardDto cardDto = new CardDto();

        when(cardService.getUserCard(userId, cardId)).thenReturn(Optional.of(card));
        when(cardMapper.toDto(card)).thenReturn(cardDto);

        CardDto result = userFacade.getUserCard(userId, cardId);

        assertNotNull(result);
        verify(cardMapper).toDto(card);
    }

    @Test
    void transferMoney_ShouldReturnBalance_AfterSuccessfulTransfer() {
        TransferRequest request = new TransferRequest(1L, 2L, 1L, 2L, new BigDecimal("100.00"));
        Card fromCard = Card.builder().maskedNumber("**** 1234").balance(new BigDecimal("900.00")).build();

        when(cardService.getUserCard(request.getFromUserId(), request.getFromCardId()))
                .thenReturn(Optional.of(fromCard));

        CardBalanceResponse response = userFacade.transferMoney(request);

        verify(transactionService).transfer(1L, 2L, new BigDecimal("100.00"));
        assertEquals("**** 1234", response.getMaskedNumber());
        assertEquals(new BigDecimal("900.00"), response.getBalance());
    }

    @Test
    void requestCardBlock_ShouldReturnStatusResponse() {
        Long userId = 1L;
        Long cardId = 1L;
        Card card = Card.builder().maskedNumber("**** 1111").status(CardStatus.BLOCKED).build();
        CardRequest request = CardRequest.builder().card(card).build();

        when(cardActionsService.requestCardBlock(userId, cardId)).thenReturn(request);

        CardStatusResponse response = userFacade.requestCardBlock(userId, cardId);

        assertEquals("**** 1111", response.getMaskedNumber());
        assertEquals(CardStatus.BLOCKED, response.getCardStatus());
    }
}
