package com.bank.api.cards.dto;

import com.bank.api.cards.util.CardRequestType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardRequestDto {

    private Long id;

    private String maskedNumber;

    private Long requestorId;

    private CardRequestType requestType;

    private LocalDateTime createdAt;

    private LocalDateTime processedAt;
}
