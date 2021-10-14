package com.example.youtubedb.config;

import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import com.example.youtubedb.config.jwt.time.RealTime;
import com.example.youtubedb.controller.AuthenticationArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SpringConfig implements WebMvcConfigurer {
    private final AuthenticationArgumentResolver authenticationArgumentResolver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationArgumentResolver);
    }

    @Bean
    CurrentTimeServer currentTimeServer() {
        return new RealTime();
    }
}
