package com.bank.api.cards.mapper;

import com.bank.api.cards.dto.UserAdminDto;
import com.bank.api.cards.dto.UserDto;
import com.bank.api.cards.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    UserAdminDto toAdminDto(User user);
}
