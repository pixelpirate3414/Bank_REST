package com.bank.api.cards.dto.response;

import com.bank.api.cards.util.CardStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardStatusResponse {

    private String maskedNumber;

    private CardStatus cardStatus;
}
