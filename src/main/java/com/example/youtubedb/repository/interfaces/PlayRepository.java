package com.example.youtubedb.repository.interfaces;

import com.example.youtubedb.domain.Play;

import java.util.Optional;

public interface PlayRepository {
    Play save(Play play);
    Optional<Play> findById(Long id);
    void delete(Play play);
}
