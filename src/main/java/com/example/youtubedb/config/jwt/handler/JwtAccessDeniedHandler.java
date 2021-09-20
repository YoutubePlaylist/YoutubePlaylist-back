package com.example.youtubedb.config.jwt.handler;

import com.example.youtubedb.dto.error.AuthenticationEntryPointFailResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper;
    @Autowired
    public JwtAccessDeniedHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(
                mapper.writeValueAsString(AuthenticationEntryPointFailResponseDto.builder()
                        .status(HttpStatus.FORBIDDEN.value())
                        .message(accessDeniedException.getMessage())
                        .build())
        );
    }
}
