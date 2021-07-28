package com.example.youtubedb.repository;

import com.example.youtubedb.domain.Play;

public interface PlayRepository {
    Play save(Play play);
    void deleteById(Long id);
}
