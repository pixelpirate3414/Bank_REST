package com.bank.api.cards.service.core.impl;

import com.bank.api.cards.entity.Card;
import com.bank.api.cards.entity.CardRequest;
import com.bank.api.cards.entity.User;
import com.bank.api.cards.repository.CardRequestRepository;
import com.bank.api.cards.service.core.CardRequestsService;
import com.bank.api.cards.util.CardRequestType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Сервис для управления запросами на блокировку и разблокировку карт.
 * Обеспечивает сохранение и обновление данных о статусах запросов.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardRequestsServiceImpl implements CardRequestsService {

    private final CardRequestRepository cardRequestRepository;

    /**
     * Получает запрос на блокировку карты по его ID.
     * @param requestId ID запроса.
     * @return Optional с найденным запросом.
     */
    @Override
    public Optional<CardRequest> getCardBlockRequest(Long requestId) {
        return cardRequestRepository.findByIdAndRequestType(requestId, CardRequestType.BLOCK);
    }

    /**
     * Получает страницу запросов на блокировку.
     * @param pageable параметры пагинации.
     * @return страница запросов.
     */
    @Override
    public Page<CardRequest> getCardBlockRequests(Pageable pageable) {
        return cardRequestRepository.findAllByRequestType(CardRequestType.BLOCK, pageable);
    }

    /**
     * Создает новый запрос на блокировку карты.
     * @param user пользователь, совершающий запрос.
     * @param card карта, подлежащая блокировке.
     * @return сохраненный запрос.
     */
    @Override
    @Transactional
    public CardRequest addCardBlockRequest(User user, Card card) {
        CardRequest cardRequest = CardRequest.builder()
                .requestedBy(user)
                .card(card)
                .requestType(CardRequestType.BLOCK)
                .build();

        return cardRequestRepository.save(cardRequest);
    }

    /**
     * Создает новый запрос на разблокировку карты.
     * @param user пользователь, совершающий запрос.
     * @param card карта, подлежащая разблокировке.
     * @return сохраненный запрос.
     */
    @Override
    @Transactional
    public CardRequest addCardReactivationRequest(User user, Card card) {
        CardRequest cardRequest = CardRequest.builder()
                .requestedBy(user)
                .card(card)
                .requestType(CardRequestType.REACTIVATE)
                .build();

        return cardRequestRepository.save(cardRequest);
    }

    /**
     * Удаляет запрос на блокировку.
     * @param requestId ID запроса.
     */
    @Override
    @Transactional
    public void deleteCardBlockRequest(Long requestId) {
        cardRequestRepository.deleteById(requestId);
    }

    /**
     * Обновляет существующий запрос.
     * @param cardRequest объект запроса с обновленными данными.
     * @return обновленный запрос.
     */
    @Override
    @Transactional
    public CardRequest updateCardRequest(CardRequest cardRequest) {
        return cardRequestRepository.save(cardRequest);
    }
}
