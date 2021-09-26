package com.example.youtubedb.config.jwt.time;

import java.time.Instant;

@FunctionalInterface
public interface CurrentTimeServer {
	Instant now();
}
