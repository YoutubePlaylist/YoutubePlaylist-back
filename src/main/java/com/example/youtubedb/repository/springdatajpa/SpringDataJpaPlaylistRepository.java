package com.example.youtubedb.repository.springdatajpa;

import com.example.youtubedb.domain.Playlist;
import com.example.youtubedb.repository.interfaces.PlaylistRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaPlaylistRepository extends JpaRepository<Playlist, Long>, PlaylistRepository {
}
