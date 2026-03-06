package com.bank.api.cards.service.core;

import com.bank.api.cards.entity.Card;
import com.bank.api.cards.entity.CardRequest;
import com.bank.api.cards.entity.User;
import com.bank.api.cards.repository.CardRequestRepository;
import com.bank.api.cards.service.core.impl.CardRequestsServiceImpl;
import com.bank.api.cards.util.CardRequestType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardRequestsServiceTest {

    @Mock
    private CardRequestRepository cardRequestRepository;

    @InjectMocks
    private CardRequestsServiceImpl cardRequestsService;

    @Test
    void addCardBlockRequest_ShouldSaveWithBlockType() {
        User user = new User();
        Card card = new Card();

        cardRequestsService.addCardBlockRequest(user, card);

        ArgumentCaptor<CardRequest> captor = ArgumentCaptor.forClass(CardRequest.class);
        verify(cardRequestRepository).save(captor.capture());

        assertEquals(CardRequestType.BLOCK, captor.getValue().getRequestType());
    }

    @Test
    void addCardReactivationRequest_ShouldSaveWithReactivateType() {
        User user = new User();
        Card card = new Card();

        cardRequestsService.addCardReactivationRequest(user, card);

        ArgumentCaptor<CardRequest> captor = ArgumentCaptor.forClass(CardRequest.class);
        verify(cardRequestRepository).save(captor.capture());

        assertEquals(CardRequestType.REACTIVATE, captor.getValue().getRequestType());
    }

    @Test
    void getCardBlockRequest_ShouldReturnOptional() {
        Long requestId = 1L;
        when(cardRequestRepository.findByIdAndRequestType(requestId, CardRequestType.BLOCK))
                .thenReturn(Optional.of(new CardRequest()));

        Optional<CardRequest> result = cardRequestsService.getCardBlockRequest(requestId);

        assertTrue(result.isPresent());
    }
}
