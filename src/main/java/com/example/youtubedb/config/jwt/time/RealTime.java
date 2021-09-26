package com.example.youtubedb.config.jwt.time;

import java.time.Instant;

public class RealTime implements CurrentTimeServer {
	@Override
	public Instant now() {
		return Instant.now();
	}
}
