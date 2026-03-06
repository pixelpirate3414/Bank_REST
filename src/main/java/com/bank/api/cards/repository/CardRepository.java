package com.bank.api.cards.repository;

import com.bank.api.cards.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("SELECT c FROM Card c WHERE c.user.id = :userId AND c.id = :cardId")
    Optional<Card> findByUserIdAndCardId(@Param("userId") Long userId, @Param("cardId") Long cardId);

    Page<Card> findAllByUserId(Long userId, Pageable pageable);

    @Modifying
    @Query("""
       update Card c
       set c.status = 'EXPIRED'
       where c.expiredAt < :today
       and c.status <> 'EXPIRED'
       """)
    int expireCards(LocalDate today);
}
