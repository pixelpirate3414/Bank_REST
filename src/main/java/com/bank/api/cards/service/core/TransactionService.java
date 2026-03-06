package com.bank.api.cards.service.core;

import com.bank.api.cards.dto.TransferDto;
import com.bank.api.cards.entity.Card;

import java.math.BigDecimal;

public interface TransactionService {

    TransferDto transfer(Long toCardId, Long fromCardId, BigDecimal amount);
}
