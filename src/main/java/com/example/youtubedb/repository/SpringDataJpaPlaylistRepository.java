package com.example.youtubedb.repository;

import com.example.youtubedb.domain.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaPlaylistRepository extends JpaRepository<Playlist, Long>, PlaylistRepository {
}
