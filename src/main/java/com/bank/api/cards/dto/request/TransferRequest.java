package com.bank.api.cards.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    @NotNull
    private Long fromUserId;

    @NotNull
    private Long toUserId;
    
    @NotNull
    private Long fromCardId;

    @NotNull
    private Long toCardId;

    @PositiveOrZero
    private BigDecimal amount;
}
