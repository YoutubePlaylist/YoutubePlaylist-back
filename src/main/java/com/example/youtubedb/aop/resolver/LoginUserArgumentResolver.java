package com.example.youtubedb.aop.resolver;

import com.example.youtubedb.config.jwt.TokenProvider;
import com.example.youtubedb.domain.member.LoginUser;
import com.example.youtubedb.exception.DoNotChangePasswordException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper;
    private final TokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isString = String.class.equals(parameter.getParameterType());

        return isLoginUserAnnotation && isString;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorizationHeader = webRequest.getHeader("Authorization");


        if (authorizationHeader == null) {
            throw new DoNotChangePasswordException();
        }

        //TODO : bearer 타입 아닐 때 생각!
        //앞에 Bearer 떼기
        String jwtToken = authorizationHeader.substring(7);

        Authentication authentication = tokenProvider.getAuthentication(jwtToken);
        return authentication.getName();

    }
}
