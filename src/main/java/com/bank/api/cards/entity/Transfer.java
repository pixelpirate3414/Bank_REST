package com.bank.api.cards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "from_card_id",
            referencedColumnName = "card_id"
    )
    private Card fromCard;

    @ManyToOne
    @JoinColumn(
            name = "to_card_id",
            referencedColumnName = "card_id"
    )
    private Card toCard;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(
            name = "operation_date",
            nullable = false
    )
    private LocalDateTime operationDate;

    @PrePersist
    public void prePersist() {
        this.operationDate = LocalDateTime.now();
    }
}
