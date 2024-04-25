package com.beside.archivist.config.filters;

import com.beside.archivist.dto.exception.ExceptionDto;
import com.beside.archivist.exception.common.ExceptionCode;
import com.beside.archivist.exception.users.InvalidTokenException;
import com.beside.archivist.exception.users.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response); // go to next filter
        } catch (TokenExpiredException e) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e.getExceptionCode());
        } catch (InvalidTokenException e) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e.getExceptionCode());
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, ExceptionCode exceptionCode) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(status.value());
        response.setContentType("application/json; charset=UTF-8");

        final ExceptionDto responseError = ExceptionDto.builder()
                .statusCode(exceptionCode.getStatus().value())
                .message(exceptionCode.getMessage())
                .build();
        response.getWriter().write(mapper.writeValueAsString(responseError));
    }
}
