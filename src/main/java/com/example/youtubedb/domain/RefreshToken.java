package com.example.youtubedb.domain;


import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("refresh")
public class RefreshToken {

    @Id
    private String pckey;
    private String phonekey;



    @Builder
    public RefreshToken(String pckey, String phonekey) {
        this.pckey = pckey;
        this.phonekey = phonekey;
    }
}
