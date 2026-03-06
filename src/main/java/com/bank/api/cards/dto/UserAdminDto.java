package com.bank.api.cards.dto;

import com.bank.api.cards.util.UserRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAdminDto {
    private Long id;

    private String userName;

    private UserRole role;

    private List<CardDto> cards;
}
