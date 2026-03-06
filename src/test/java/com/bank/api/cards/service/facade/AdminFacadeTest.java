package com.bank.api.cards.service.facade;

import com.bank.api.cards.dto.CardAdminDto;
import com.bank.api.cards.dto.CardRequestDto;
import com.bank.api.cards.dto.UserAdminDto;
import com.bank.api.cards.entity.Card;
import com.bank.api.cards.entity.CardRequest;
import com.bank.api.cards.entity.User;
import com.bank.api.cards.exception.CardNotFoundException;
import com.bank.api.cards.exception.CardRequestNotFoundException;
import com.bank.api.cards.exception.UserNotFoundException;
import com.bank.api.cards.mapper.CardMapper;
import com.bank.api.cards.mapper.CardRequestsMapper;
import com.bank.api.cards.mapper.UserMapper;
import com.bank.api.cards.service.core.*;
import com.bank.api.cards.service.facade.impl.AdminFacadeImpl;
import com.bank.api.cards.util.CardRequestStatus;
import com.bank.api.cards.util.CardStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminFacadeTest {

    @Mock
    private CardService cardService;
    @Mock
    private UserService userService;
    @Mock
    private CardRequestsService cardRequestsService;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private CardRequestsMapper cardRequestsMapper;

    @InjectMocks
    private AdminFacadeImpl adminFacade;

    @Test
    void getCard_shouldReturnCardDto() {
        Long cardId = 1L;
        Card card = new Card();
        CardAdminDto dto = new CardAdminDto();

        when(cardService.getCard(cardId)).thenReturn(Optional.of(card));
        when(cardMapper.toAdminDto(card)).thenReturn(dto);

        CardAdminDto result = adminFacade.getCard(cardId);

        assertEquals(dto, result);
        verify(cardService).getCard(cardId);
        verify(cardMapper).toAdminDto(card);
    }

    @Test
    void getCard_shouldThrowException_whenCardNotFound() {
        Long cardId = 1L;

        when(cardService.getCard(cardId)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class,
                () -> adminFacade.getCard(cardId));
    }

    @Test
    void getAllCards_shouldReturnPageOfDtos() {
        Card card = new Card();
        CardAdminDto dto = new CardAdminDto();

        Page<Card> page = new PageImpl<>(List.of(card));

        when(cardService.getCards(any(PageRequest.class))).thenReturn(page);
        when(cardMapper.toAdminDto(card)).thenReturn(dto);

        Page<CardAdminDto> result = adminFacade.getAllCards(0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals(dto, result.getContent().get(0));
    }

    @Test
    void createCard_shouldCreateAndReturnDto() {
        Long userId = 1L;
        Card card = new Card();
        CardAdminDto dto = new CardAdminDto();

        when(cardService.createCard(userId)).thenReturn(card);
        when(cardMapper.toAdminDto(card)).thenReturn(dto);

        CardAdminDto result = adminFacade.createCard(userId);

        assertEquals(dto, result);
        verify(cardService).createCard(userId);
    }

    @Test
    void blockCard_shouldBlockCardAndUpdateRequest() {
        Long requestId = 1L;

        Card card = new Card();
        card.setId(10L);

        CardRequest request = new CardRequest();
        request.setCard(card);

        Card updatedCard = new Card();
        CardAdminDto dto = new CardAdminDto();

        when(cardRequestsService.getCardBlockRequest(requestId))
                .thenReturn(Optional.of(request));

        when(cardService.updateStatus(card.getId(), CardStatus.BLOCKED))
                .thenReturn(updatedCard);

        when(cardMapper.toAdminDto(updatedCard)).thenReturn(dto);

        CardAdminDto result = adminFacade.blockCard(requestId);

        assertEquals(dto, result);

        verify(cardService).updateStatus(card.getId(), CardStatus.BLOCKED);
        verify(cardRequestsService).updateCardRequest(request);

        assertEquals(CardRequestStatus.PROCESSED, request.getStatus());
    }

    @Test
    void activateCard_shouldActivateCard() {
        Long cardId = 1L;
        Card card = new Card();
        CardAdminDto dto = new CardAdminDto();

        when(cardService.updateStatus(cardId, CardStatus.ACTIVE)).thenReturn(card);
        when(cardMapper.toAdminDto(card)).thenReturn(dto);

        CardAdminDto result = adminFacade.activateCard(cardId);

        assertEquals(dto, result);
        verify(cardService).updateStatus(cardId, CardStatus.ACTIVE);
    }

    @Test
    void deleteCard_shouldCallService() {
        Long cardId = 1L;

        adminFacade.deleteCard(cardId);

        verify(cardService).deleteCard(cardId);
    }

    @Test
    void getUser_shouldReturnUserDto() {
        Long userId = 1L;
        User user = new User();
        UserAdminDto dto = new UserAdminDto();

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toAdminDto(user)).thenReturn(dto);

        UserAdminDto result = adminFacade.getUser(userId);

        assertEquals(dto, result);
    }

    @Test
    void getUser_shouldThrowException_whenUserNotFound() {
        Long userId = 1L;

        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> adminFacade.getUser(userId));
    }

    @Test
    void deleteUser_shouldCallService() {
        Long userId = 1L;

        adminFacade.deleteUser(userId);

        verify(userService).deleteUserById(userId);
    }

    @Test
    void getBlockRequest_shouldReturnDto() {
        Long requestId = 1L;
        CardRequest request = new CardRequest();
        CardRequestDto dto = new CardRequestDto();

        when(cardRequestsService.getCardBlockRequest(requestId))
                .thenReturn(Optional.of(request));

        when(cardRequestsMapper.toDto(request)).thenReturn(dto);

        CardRequestDto result = adminFacade.getBlockRequest(requestId);

        assertEquals(dto, result);
    }

    @Test
    void getBlockRequest_shouldThrowException_whenNotFound() {
        Long requestId = 1L;

        when(cardRequestsService.getCardBlockRequest(requestId))
                .thenReturn(Optional.empty());

        assertThrows(CardRequestNotFoundException.class,
                () -> adminFacade.getBlockRequest(requestId));
    }
}
