package com.bank.api.cards.controller;

import com.bank.api.cards.dto.UserAdminDto;
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
@RequestMapping("${app.api.version}/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@Validated
@RequiredArgsConstructor
public class AdminUserController {
    
    private final AdminFacade adminFacade;

    @GetMapping("/{userId}")
    public ResponseEntity<UserAdminDto> getUser(@PathVariable @NotNull Long userId) {
        return ResponseEntity.ok(
                adminFacade.getUser(userId)
        );
    }
    
    @GetMapping
    public ResponseEntity<Page<UserAdminDto>> getAllUsers(
        @RequestParam @Min(0) int page, 
        @RequestParam @Positive int size
    ) {
        return ResponseEntity.ok(
                adminFacade.getUsers(page, size)
        );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NotNull Long userId) {
        adminFacade.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }
}
