package com.bank.api.cards.controller;

import com.bank.api.cards.dto.CardDto;
import com.bank.api.cards.dto.response.CardStatusResponse;
import com.bank.api.cards.service.facade.UserFacade;
import com.bank.api.cards.util.SecurityUtils;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api.version}/cards")
@RequiredArgsConstructor
public class CardController {
    
    private final UserFacade userFacade;

    private final SecurityUtils securityUtils;

    @GetMapping("/{cardId}")
    public ResponseEntity<CardDto> viewUserCard(@PathVariable @NotNull Long cardId) {
        Long userId = securityUtils.getCurrentUserId();

        return ResponseEntity.ok(
            userFacade.getUserCard(userId, cardId)
        );
    }
    
    @GetMapping
    public ResponseEntity<Page<CardDto>> viewAllUserCards(
        @RequestParam @Min(0) int page, 
        @RequestParam @Positive int size
    ) {
        Long userId = securityUtils.getCurrentUserId();

        return ResponseEntity.ok(
            userFacade.getUserCards(userId, PageRequest.of(page, size))
        );
    }
    
    @PostMapping("/{cardId}/block-request")
    public ResponseEntity<CardStatusResponse> requestCardBlock(@PathVariable @NotNull Long cardId) {
        Long userId = securityUtils.getCurrentUserId();

        return ResponseEntity.ok(
            userFacade.requestCardBlock(cardId, userId)
        );
    }

    @PostMapping("/{cardId}/reactivation-request")
    public ResponseEntity<CardStatusResponse> requestCardReactivation(@PathVariable @NotNull Long cardId) {
        Long userId = securityUtils.getCurrentUserId();

        return ResponseEntity.ok(
                userFacade.requestCardReactivation(cardId, userId)
        );
    }
}
