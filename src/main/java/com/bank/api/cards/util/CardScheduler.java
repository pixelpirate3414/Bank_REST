package com.bank.api.cards.util;

import com.bank.api.cards.service.facade.CardFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CardScheduler {

    private final CardFacade cardFacade;

    @Scheduled(cron = "${app.scheduling.card-expiration-cron}")
    @Transactional
    public void expiredCards() {
        cardFacade.setExpiredCards();
    }
}
