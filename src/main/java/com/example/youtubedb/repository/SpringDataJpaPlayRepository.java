package com.example.youtubedb.repository;

import com.example.youtubedb.domain.Play;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaPlayRepository extends JpaRepository<Play, Long>, PlayRepository {
}
