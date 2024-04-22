package com.beside.archivist.config;

import com.beside.archivist.dto.exception.ExceptionDto;
import com.beside.archivist.exception.common.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    /**
     * 인증을 요구하는 API 에서 토큰이 없거나 JWT 가 아닌 경우
     * @param request that resulted in an <code>AuthenticationException</code>
     * @param response so that the user agent can begin authentication
     * @param authException that caused the invocation
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json; charset=UTF-8");

        final ExceptionDto exceptionDto = ExceptionDto.builder()
                .statusCode(ExceptionCode.MISSING_AUTHENTICATION.getStatus().value())
                .message(ExceptionCode.MISSING_AUTHENTICATION.getMessage())
                .build();

        response.getWriter().write(mapper.writeValueAsString(exceptionDto));
    }
}
