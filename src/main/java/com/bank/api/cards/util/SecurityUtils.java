package com.bank.api.cards.util;

import com.bank.api.cards.entity.User;
import com.bank.api.cards.exception.UserNotFoundException;
import com.bank.api.cards.service.core.UserService;
import com.bank.api.cards.service.facade.UserFacade;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    @Value("${security.card.secrete}")
    private String cardSecrete;

    private final String ALGORITHM = "AES";

    private SecretKey cardSecretKey;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    /**
     * Инициализирует секретный ключ шифрования после внедрения зависимостей.
     */
    @PostConstruct
    public void init() {
        cardSecretKey = generateSecretKey(cardSecrete);
    }

    /**
     * Хэширует пароль пользователя.
     * @param password открытый пароль.
     * @return хэшированный пароль.
     */
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Маскирует номер карты, оставляя видимыми только последние 4 цифры.
     * @param cardNumber полный номер карты.
     * @return маскированный номер.
     */
    public String maskCard(String cardNumber) {
        String part = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + part;
    }

    /**
     * Шифрует номер карты с использованием алгоритма AES.
     * @param cardNumber открытый номер карты.
     * @return зашифрованная строка в формате Base64.
     * @throws RuntimeException при ошибке шифрования.
     */
    public String encodeCardNumber(String cardNumber) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, cardSecretKey);
            byte[] encrypted = cipher.doFinal(cardNumber.getBytes());

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (
            NoSuchPaddingException
            | NoSuchAlgorithmException
            | InvalidKeyException
            | IllegalBlockSizeException
            | BadPaddingException e
        ) {
            throw new RuntimeException("Error encrypting card number", e.getCause());
        }
    }


    private SecretKey generateSecretKey(String secrete) {
        byte[] keyBytes = Arrays.copyOf(secrete.getBytes(), 32);

        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    /**
     * Извлекает ID текущего авторизованного пользователя из SecurityContext.
     * @return ID пользователя.
     * @throws UserNotFoundException если пользователь не найден в БД.
     */
    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assert auth != null;
        String username = auth.getName();

        return userService.getUserByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
