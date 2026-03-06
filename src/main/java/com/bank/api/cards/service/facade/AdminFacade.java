package com.bank.api.cards.service.facade;

import com.bank.api.cards.dto.CardAdminDto;
import com.bank.api.cards.dto.CardRequestDto;
import com.bank.api.cards.dto.UserAdminDto;
import com.bank.api.cards.entity.CardRequest;
import org.springframework.data.domain.Page;

public interface AdminFacade {

    CardAdminDto getCard(Long cardId);

    Page<CardAdminDto> getAllCards(int page, int size);

    Page<CardAdminDto> getAllUserCards(Long userId, int page, int size);

    CardAdminDto createCard(Long userId);

    CardAdminDto blockCard(Long cardId);

    CardAdminDto activateCard(Long cardId);

    void deleteCard(Long cardId);

    UserAdminDto getUser(Long userId);

    Page<UserAdminDto> getUsers(int page, int size);

    void deleteUser(Long userId);

    CardRequestDto getBlockRequest(Long requestId);

    Page<CardRequestDto> getAllBlockRequests(int page, int size);
}
