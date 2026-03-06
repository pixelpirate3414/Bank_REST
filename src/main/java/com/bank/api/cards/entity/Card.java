package com.bank.api.cards.entity;

import com.bank.api.cards.util.CardStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long id;

    @Column(
            name = "number",
            nullable = false,
            unique = true
    )
    private String number;

    @Column(
            name = "masked_number",
            nullable = false
    )
    private String maskedNumber;

    @Column(
            name = "expired_at",
            nullable = false
    )
    private LocalDate expiredAt;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false
    )
    private CardStatus status;

    @Column(
            name = "balance",
            nullable = false
    )
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
