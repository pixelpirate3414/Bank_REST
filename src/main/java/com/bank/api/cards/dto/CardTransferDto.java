package com.bank.api.cards.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardTransferDto {
    private Long id;

    private String maskedNumber;
}
