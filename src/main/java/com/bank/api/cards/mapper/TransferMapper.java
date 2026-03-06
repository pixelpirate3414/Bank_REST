package com.bank.api.cards.mapper;

import com.bank.api.cards.dto.TransferDto;
import com.bank.api.cards.entity.Transfer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransferMapper {
    TransferDto toDto(Transfer transfer);

    Transfer toEntity(TransferDto transferDto);
}
