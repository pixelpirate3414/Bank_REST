package com.bank.api.cards.dto;

import com.bank.api.cards.util.CardStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardAdminDto {
    private Long id;

    private String maskedNumber;

    private CardStatus status;

    private BigDecimal balance;

    private UserDto owner;
}
