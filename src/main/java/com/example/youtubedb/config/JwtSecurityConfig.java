package com.example.youtubedb.config;

import com.example.youtubedb.config.jwt.JwtResolver;
import com.example.youtubedb.config.jwt.TokenProvider;
import com.example.youtubedb.config.jwt.filter.ExceptionHandlerFilter;
import com.example.youtubedb.config.jwt.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private final JwtResolver jwtResolver;

	@Override
	public void configure(HttpSecurity http) {
		JwtFilter customFilter = new JwtFilter(jwtResolver);
		ExceptionHandlerFilter exceptionHandlerFilter = new ExceptionHandlerFilter();
		http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(exceptionHandlerFilter, JwtFilter.class);
	}
}
