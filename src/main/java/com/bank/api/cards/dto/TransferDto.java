package com.bank.api.cards.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {
    private Long id;

    private CardTransferDto fromCard;

    private CardTransferDto toCard;

    private BigDecimal amount;

    private LocalDateTime operationDate;
}
