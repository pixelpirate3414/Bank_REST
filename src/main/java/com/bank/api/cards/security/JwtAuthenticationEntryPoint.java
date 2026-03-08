package com.bank.api.cards.security;

import com.bank.api.cards.dto.response.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
/**
 *  Пользовательская реализация интерфейса {@link AuthenticationEntryPoint},
 *  отвечающая за обработку исключений аутентификации и возврат ответов о несанкционированном доступе.
 *  Этот класс используется фреймворком Spring Security для обработки ситуаций, когда пользователь не аутентифицирован,
 *  и возвращает JSON-ответ с кодом состояния 401 Unauthorized.
 * */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
        HttpServletRequest request, 
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        ExceptionResponse errorResponse = new ExceptionResponse(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                authException.getMessage(),
                request.getServletPath()
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        mapper.writeValue(response.getWriter(), errorResponse);
    }
    
}
