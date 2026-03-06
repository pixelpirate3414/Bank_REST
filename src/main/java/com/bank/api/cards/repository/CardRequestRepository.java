package com.bank.api.cards.repository;

import com.bank.api.cards.entity.CardRequest;
import com.bank.api.cards.util.CardRequestType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRequestRepository extends JpaRepository<CardRequest, Long> {

    Optional<CardRequest> findByIdAndRequestType(Long requestId, CardRequestType requestType);

    Page<CardRequest> findAllByRequestType(CardRequestType requestType, Pageable pageable);
}
