package com.bank.api.cards.mapper;

import com.bank.api.cards.dto.CardRequestDto;
import com.bank.api.cards.entity.CardRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardRequestsMapper {

    CardRequest fromDto(CardRequestDto dto);

    CardRequestDto toDto(CardRequest entity);
}
