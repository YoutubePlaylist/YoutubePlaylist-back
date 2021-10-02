package com.example.youtubedb.config.jwt;

public interface TokenParser<T> {
	T parse(String tokenString);
}