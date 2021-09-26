package com.example.youtubedb.config.jwt.time;

import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
public class ConstantTime implements CurrentTimeServer {
	private final Instant currentTime;

	@Override
	public Instant now() {
		return currentTime;
	}
}
