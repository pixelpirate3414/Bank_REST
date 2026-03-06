package com.bank.api.cards.service.core;

import com.bank.api.cards.dto.TransferDto;
import com.bank.api.cards.entity.Card;
import com.bank.api.cards.entity.Transfer;
import com.bank.api.cards.exception.CardOperationException;
import com.bank.api.cards.mapper.TransferMapper;
import com.bank.api.cards.repository.TransferRepository;
import com.bank.api.cards.service.core.impl.*;
import com.bank.api.cards.util.CardStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private CardService cardService;
    @Mock
    private TransferMapper transferMapper;
    @Mock
    private TransferRepository transferRepository;
    @Mock
    private CardActionsService cardActionsService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void transfer_ShouldThrowException_WhenCardIsBlocked() {
        Card fromCard = Card.builder().status(CardStatus.BLOCKED).build();
        Card toCard = Card.builder().status(CardStatus.ACTIVE).build();

        when(cardService.getCard(1L)).thenReturn(Optional.of(fromCard));
        when(cardService.getCard(2L)).thenReturn(Optional.of(toCard));

        assertThrows(CardOperationException.class, () ->
                transactionService.transfer(2L, 1L, new BigDecimal("100.00"))
        );
    }

    @Test
    void transfer_ShouldThrowException_WhenInsufficientFunds() {
        Card fromCard = Card.builder().status(CardStatus.ACTIVE).balance(new BigDecimal("50.00")).build();
        Card toCard = Card.builder().status(CardStatus.ACTIVE).balance(new BigDecimal("0.00")).build();

        when(cardService.getCard(1L)).thenReturn(Optional.of(fromCard));
        when(cardService.getCard(2L)).thenReturn(Optional.of(toCard));

        assertThrows(CardOperationException.class, () ->
                transactionService.transfer(2L, 1L, new BigDecimal("100.00"))
        );
    }

    @Test
    void transfer_ShouldExecuteSuccessfully_WhenConditionsMet() {
        BigDecimal amount = new BigDecimal("100.00");
        Card fromCard = Card.builder().status(CardStatus.ACTIVE).balance(new BigDecimal("200.00")).build();
        Card toCard = Card.builder().status(CardStatus.ACTIVE).balance(new BigDecimal("0.00")).build();

        when(cardService.getCard(1L)).thenReturn(Optional.of(fromCard));
        when(cardService.getCard(2L)).thenReturn(Optional.of(toCard));

        // Mock save returning a Transfer object
        when(transferRepository.save(any(Transfer.class))).thenReturn(new Transfer());
        when(transferMapper.toDto(any(Transfer.class))).thenReturn(new TransferDto());

        transactionService.transfer(2L, 1L, amount);

        verify(cardActionsService).updateBalance(eq(2L), any(BigDecimal.class));
        verify(cardActionsService).updateBalance(eq(1L), any(BigDecimal.class));
        verify(transferRepository).save(any(Transfer.class));
    }
}
