package com.bank.api.cards.service.facade.impl;

import com.bank.api.cards.service.core.CardActionsService;
import com.bank.api.cards.service.facade.CardFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CardFacadeImpl implements CardFacade {

    private final CardActionsService cardActionsService;

    /**
     * Выполняет проверку карт на истечение срока действия.
     * Метод находит все карты, у которых истек срок, и обновляет их статус.
     * Транзакция необходима для обеспечения атомарности обновления статусов.
     */
    @Override
    @Transactional
    public void setExpiredCards() {
        cardActionsService.findAndSetExpiredCards();
    }
}
