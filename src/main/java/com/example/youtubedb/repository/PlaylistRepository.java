package com.example.youtubedb.repository;

import com.example.youtubedb.domain.Playlist;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository {
    Playlist save(Playlist playlist);
    Optional<Playlist> findById(Long id);
    List<Playlist> findAll();
    void delete(Playlist playlist);
}
