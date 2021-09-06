package com.example.youtubedb.config.jwt.filter;

import com.example.youtubedb.dto.error.AuthenticationEntryPointFailResponseDto;
import com.example.youtubedb.dto.error.InvalidJwtFailResponseDto;
import com.example.youtubedb.exception.NotExistAuthorityException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e);
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
            setErrorResponse(HttpStatus.BAD_REQUEST, response, e);
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e);
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e);
        } catch (NotExistAuthorityException e) {
            log.error("토큰 내 권한이 없습니다.");
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e);
        } catch (RuntimeException e) {
            log.error("예상치 못한 서버 오류입니다.");
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, e);
        }
    }

    private void setErrorResponse(HttpStatus status, HttpServletResponse response, RuntimeException e) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(
                mapper.writeValueAsString(InvalidJwtFailResponseDto.builder()
                        .status(status.value())
                        .message(e.getMessage())
                        .build())
        );
    }
}
