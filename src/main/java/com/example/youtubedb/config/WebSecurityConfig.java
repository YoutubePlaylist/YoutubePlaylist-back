package com.example.youtubedb.config;

import com.example.youtubedb.config.jwt.handler.JwtAccessDeniedHandler;
import com.example.youtubedb.config.jwt.handler.JwtAuthenticationEntryPoint;
import com.example.youtubedb.config.jwt.TokenProvider;
import com.example.youtubedb.domain.member.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsUtils;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/api/member").hasAuthority(Authority.ROLE_USER.name())
                .antMatchers("/api/member/change").hasAuthority(Authority.ROLE_USER.name())
                .antMatchers("/api/member/upload").hasAuthority(Authority.ROLE_USER.name())
                .antMatchers("/api/member/**").permitAll()
                .antMatchers("/api/**").hasAuthority(Authority.ROLE_USER.name())
                .and()
                .cors()
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
