package com.example.youtubedb.service;

import com.example.youtubedb.domain.Member;
import com.example.youtubedb.domain.Play;
import com.example.youtubedb.domain.Playlist;
import com.example.youtubedb.exception.InvalidAccessException;
import com.example.youtubedb.exception.StartAndEndTimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class PlayServiceIntegrationTest {
    @Autowired
    private PlayService playService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PlaylistService playlistService;

    private String videoId;
    private long start;
    private long end;
    private String thumbnail;
    private String title;
    private String channelAvatar;

    @BeforeEach
    void setup() {
        this.videoId = "video001";
        this.start = 100;
        this.end = 1000;
        this.thumbnail = "썸네일";
        this.title = "영상1";
        this.channelAvatar = "아바타 이미지";
    }

    @Test
    void 플레이_추가() {
        // given
        Member member = memberService.registerNon("device001");
        Playlist playlist = playlistService.createPlaylist("default", "false", "OTHER", member);

        // when
        Play play = playService.addPlayToPlaylist(
                playlist,
                videoId,
                start,
                end,
                thumbnail,
                title,
                channelAvatar);

        // then
        assertAll(
                () -> assertThat(play.getTitle()).isEqualTo(title),
                () -> assertThat(play.getVideoId()).isEqualTo(videoId),
                () -> assertThat(play.getStart()).isEqualTo(start),
                () -> assertThat(play.getEnd()).isEqualTo(end),
                () -> assertThat(play.getThumbnail()).isEqualTo(thumbnail),
                () -> assertThat(play.getSequence()).isEqualTo(1),
                () -> assertThat(play.getChannelAvatar()).isEqualTo(channelAvatar),
                () -> assertThat(play.getPlaylist().getId()).isEqualTo(playlist.getId())
        );
    }

    @Test
    void 플레이_추가_시간예외() {
        // given
        Member member = memberService.registerNon("device001");
        Playlist playlist = playlistService.createPlaylist("default", "false", "OTHER", member);

        // when
        Exception e = assertThrows(StartAndEndTimeException.class , () ->
                playService.addPlayToPlaylist(
                        playlist,
                        videoId,
                        0L,
                        -5L,
                        thumbnail,
                        title,
                        channelAvatar)
                );

        // then
        assertThat(e.getMessage()).isEqualTo(StartAndEndTimeException.getErrorMessage());
    }

    @Test
    void 영상목록_조회() {
        // given
        Member member = memberService.registerNon("device001");
        Playlist playlist = playlistService.createPlaylist("default", "false", "OTHER", member);
        playService.addPlayToPlaylist(
                playlist,
                videoId,
                start,
                end,
                thumbnail,
                title,
                channelAvatar);

        // when
        List<Play> plays = playService.getPlaysInPlaylist(playlist, member);

        // then
        assertAll(
                () -> assertThat(plays.size()).isEqualTo(1),
                () -> assertThat(plays.get(0).getTitle()).isEqualTo(title)
        );
    }

    @Test
    void 영상목록_조회_접근권한X() {
        // given
        Member member = memberService.registerNon("device001");
        Member other = memberService.registerNon("device002");
        Playlist playlist = playlistService.createPlaylist("default", "false", "OTHER", member);
        playService.addPlayToPlaylist(
                playlist,
                videoId,
                start,
                end,
                thumbnail,
                title,
                channelAvatar);

        // when
        Exception e = assertThrows(InvalidAccessException.class, () -> playService.getPlaysInPlaylist(playlist, other));

        // then
        assertThat(e.getMessage()).isEqualTo(InvalidAccessException.getErrorMessage());
    }
}
