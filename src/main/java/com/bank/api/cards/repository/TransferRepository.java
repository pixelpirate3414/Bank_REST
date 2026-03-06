package com.bank.api.cards.repository;

import com.bank.api.cards.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    
}
