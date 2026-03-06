package com.bank.api.cards.dto;

import com.bank.api.cards.util.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    private String userName;

    private UserRole role;
}
