package com.bank.api.cards.mapper;

import com.bank.api.cards.dto.CardAdminDto;
import com.bank.api.cards.dto.CardDto;
import com.bank.api.cards.dto.CardTransferDto;
import com.bank.api.cards.entity.Card;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CardDto toDto(Card card);

    Card toEntity(CardDto cardDto);

    CardAdminDto toAdminDto(Card card);

    CardTransferDto toTransferDto(Card card);
}
