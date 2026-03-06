package com.bank.api.cards.service.facade;

import com.bank.api.cards.service.core.CardActionsService;
import com.bank.api.cards.service.facade.impl.CardFacadeImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardFacadeTest {

    @Mock
    private CardActionsService cardActionsService;

    @InjectMocks
    private CardFacadeImpl cardFacade;

    @Test
    void setExpiredCards_ShouldDelegateToCardActionsService() {
        cardFacade.setExpiredCards();

        verify(cardActionsService, times(1)).findAndSetExpiredCards();
    }
}
