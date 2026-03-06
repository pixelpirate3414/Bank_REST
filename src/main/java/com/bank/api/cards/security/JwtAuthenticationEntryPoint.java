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
 * A custom implementation of the {@link AuthenticationEntryPoint} interface, 
 * responsible for handling authentication exceptions and returning unauthorized responses.
 * 
 * This class is used by the Spring Security framework to handle situations where a user is not authenticated, 
 * and returns a JSON response with a 401 Unauthorized status code.
 */
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
