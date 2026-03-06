package com.bank.api.cards.controller;

import com.bank.api.cards.dto.CardAdminDto;
import com.bank.api.cards.dto.CardRequestDto;
import com.bank.api.cards.service.facade.AdminFacade;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api.version}/admin/cards")
@PreAuthorize("hasRole('ADMIN')")
@Validated
@RequiredArgsConstructor
public class AdminCardController {
    
    private final AdminFacade adminFacade;

    @GetMapping("/{cardId}")
    public ResponseEntity<CardAdminDto> getCard(@PathVariable @NotNull Long cardId) {
        return ResponseEntity.ok(
            adminFacade.getCard(cardId)
        );
    }
    
    @GetMapping
    public ResponseEntity<Page<CardAdminDto>> getAllCards(
        @RequestParam @Min(0) int page,
        @RequestParam @Positive int size
    ) {
        return ResponseEntity.ok(
            adminFacade.getAllCards(page, size)
        );
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<CardAdminDto>> getAllCardsByUser(
        @PathVariable @NotBlank Long userId,
        @RequestParam @Min(0) int page,
        @RequestParam @Positive int size
    ) {
        return ResponseEntity.ok(
            adminFacade.getAllUserCards(userId, page, size)
        );
    }
    
    @PostMapping("/user/{userId}")
    public ResponseEntity<CardAdminDto> createCard(@PathVariable @NotNull Long userId) {
        return ResponseEntity.ok(
            adminFacade.createCard(userId)
        );
    }

    @PatchMapping("/{cardId}/activate")
    public ResponseEntity<CardAdminDto> activateCard(@PathVariable @NotNull Long cardId) {
        return ResponseEntity.ok(
            adminFacade.activateCard(cardId)
        );
    }

    @PatchMapping("/block-requests/{requestId}/block")
    public ResponseEntity<CardAdminDto> blockCard(@PathVariable @NotNull Long requestId) {
        return ResponseEntity.ok(
            adminFacade.blockCard(requestId)
        );
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable @NotNull Long cardId) {
        adminFacade.deleteCard(cardId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/block-requests/{requestId}")
    public ResponseEntity<CardRequestDto> getBlockRequest(@PathVariable @NotNull Long requestId) {
        return ResponseEntity.ok(
            adminFacade.getBlockRequest(requestId)
        );
    }

    @GetMapping("/block-requests")
    public ResponseEntity<Page<CardRequestDto>> getAllBlockRequests(
            @RequestParam @Min(0) int page,
            @RequestParam @Positive int size
    ) {
        return ResponseEntity.ok(
            adminFacade.getAllBlockRequests(page, size)
        );
    }
}
