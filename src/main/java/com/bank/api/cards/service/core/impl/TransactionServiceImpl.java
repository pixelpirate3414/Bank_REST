package com.bank.api.cards.service.core.impl;

import com.bank.api.cards.dto.TransferDto;
import com.bank.api.cards.entity.Card;
import com.bank.api.cards.entity.Transfer;
import com.bank.api.cards.exception.CardNotFoundException;
import com.bank.api.cards.exception.CardOperationException;
import com.bank.api.cards.mapper.TransferMapper;
import com.bank.api.cards.repository.TransferRepository;
import com.bank.api.cards.service.core.CardActionsService;
import com.bank.api.cards.service.core.CardService;
import com.bank.api.cards.service.core.TransactionService;
import com.bank.api.cards.util.CardStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Сервис для управления финансовыми транзакциями между картами.
 * Обеспечивает выполнение переводов с гарантией согласованности данных.
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final CardService cardService;
    private final TransferMapper transferMapper;
    private final TransferRepository transferRepository;
    private final CardActionsService cardActionsService;

    /**
     * Выполняет перевод денежных средств между двумя картами.
     *
     * @param toCardId   ID карты получателя.
     * @param fromCardId ID карты отправителя.
     * @param amount     сумма перевода.
     * @return DTO информации о совершенном переводе.
     * @throws CardNotFoundException если одна из карт не найдена.
     * @throws CardOperationException если карта заблокирована или недостаточно средств.
     */
    @Override
    @Transactional
    public TransferDto transfer(Long toCardId, Long fromCardId, BigDecimal amount) {
        Card fromCard = cardService.getCard(fromCardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
        Card toCard = cardService.getCard(toCardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));


        if (fromCard.getStatus() == CardStatus.BLOCKED
                || toCard.getStatus() == CardStatus.BLOCKED) {
            throw new CardOperationException(HttpStatus.CONFLICT, "Card is blocked");
        }

        if (fromCard.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new CardOperationException(HttpStatus.CONFLICT, "Not enough money");
        }


        BigDecimal fromCardBalance = fromCard.getBalance().subtract(amount);
        BigDecimal toCardBalance = toCard.getBalance().add(amount);

        fromCard.setBalance(fromCardBalance);
        toCard.setBalance(toCardBalance);


        cardActionsService.updateBalance(toCardId, toCardBalance);
        cardActionsService.updateBalance(fromCardId, fromCardBalance);

        Transfer transfer = Transfer.builder()
                .fromCard(fromCard)
                .toCard(toCard)
                .amount(amount)
                .build();

        return transferMapper.toDto(transferRepository.save(transfer));
    }
}
