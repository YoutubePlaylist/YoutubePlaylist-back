package com.example.youtubedb.config.jwt;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class Token2Test {
    @Test
    void test1() {
        final LocalDateTime now = LocalDateTime.now();

        assertThat(new Token2("1", now, SignatureAlgorithm.HS512),
          is(new Token2("1", now, SignatureAlgorithm.HS512)));
    }
}