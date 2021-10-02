package com.example.youtubedb.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PlaylistTest {


  @Test
  void create() {
    Playlist playlist = Playlist.builder()
      .title("title")
      .isPublic(true)
      .build();

    assertThat(playlist.isPublic()).isEqualTo(true);
  }

  @Test
  void valid() {
    assertThatThrownBy(() ->
      Playlist.builder()
        .title("")
        .isPublic(true)
        .build())
      .isInstanceOf(Exception.class);

    assertThatThrownBy(() ->
      Playlist.builder()
        .title("")
        .isPublic(true)
        .build())
      .isInstanceOf(Exception.class);
  }
}