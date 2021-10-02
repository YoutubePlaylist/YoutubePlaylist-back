package com.example.youtubedb.repository.springdatajpa;

import com.example.youtubedb.domain.Play;
import com.example.youtubedb.repository.interfaces.PlayRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaPlayRepository extends JpaRepository<Play, Long>, PlayRepository {
}
