package com.example.youtubedb.service;

import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.domain.Play;
import com.example.youtubedb.domain.Playlist;
import com.example.youtubedb.dto.play.PlaySeqDto;
import com.example.youtubedb.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private String channelTitle;
    private boolean isPc;

    @BeforeEach
    void setup() {
        this.videoId = "video001";
        this.isPc =true;
        this.start = 100;
        this.end = 1000;
        this.thumbnail = "썸네일";
        this.title = "영상1";
        this.channelAvatar = "아바타 이미지";
        this.channelTitle ="채널이름1";
    }

    @Test
    void 플레이_추가() {
        // given
        Member member = memberService.registerNon("device001", isPc);
        Playlist playlist = playlistService.createPlaylist("default", false, "OTHER", member);

        // when
        Play play = playService.addPlayToPlaylist(
                playlist,
                member.getLoginId(),
                videoId,
                start,
                end,
                thumbnail,
                title,
                channelAvatar,
                channelTitle);

        // then
        assertThat(play.getTitle()).isEqualTo(title);
    }

    @Test
    void 플레이_추가_시간예외() {
        // given
        Member member = memberService.registerNon("device001", isPc);
        Playlist playlist = playlistService.createPlaylist("default", false, "OTHER", member);

        // when
        Exception e = assertThrows(StartAndEndTimeException.class , () ->
                playService.addPlayToPlaylist(
                        playlist,
                        member.getLoginId(),
                        videoId,
                        0L,
                        -5L,
                        thumbnail,
                        title,
                        channelAvatar,
                        channelTitle)
                );

        // then
        assertThat(e.getMessage()).isEqualTo(StartAndEndTimeException.getErrorMessage());
    }

    @Test
    void 영상목록_조회() {
        // given
        Member member = memberService.registerNon("device001", isPc);
        Playlist playlist = playlistService.createPlaylist("default", false, "OTHER", member);
        playService.addPlayToPlaylist(
                playlist,
                member.getLoginId(),
                videoId,
                start,
                end,
                thumbnail,
                title,
                channelAvatar,
                channelTitle);

        // when
        List<Play> plays = playService.getPlaysInPlaylist(playlist, member.getLoginId());

        // then
        assertAll(
                () -> assertThat(plays.size()).isEqualTo(1),
                () -> assertThat(plays.get(0).getTitle()).isEqualTo(title)
        );
    }

    @Test
    void 영상_시간_수정() {
        // given
        Member member = memberService.registerNon("device001", isPc);
        Playlist playlist = playlistService.createPlaylist("default", false, "OTHER", member);
        Play play = playService.addPlayToPlaylist(
                playlist,
                member.getLoginId(),
                videoId,
                start,
                end,
                thumbnail,
                title,
                channelAvatar,
                channelTitle);

        // when
        playService.editTime(play, member.getLoginId(), 50L, 100L);
        Play editedPlay = playService.getPlayById(play.getId());

        // then
        assertAll(
                () -> assertThat(editedPlay.getStart()).isEqualTo(50L),
                () -> assertThat(editedPlay.getEnd()).isEqualTo(100L)
        );
    }

    @Test
    void 영상_순서_수정() {
        // given
        Member member = memberService.registerNon("device001", isPc);
        Playlist playlist = playlistService.createPlaylist("default", false, "OTHER", member);
        Play play1 = playService.addPlayToPlaylist(
                playlist,
                member.getLoginId(),
                videoId,
                start,
                end,
                thumbnail,
                "play1",
                channelAvatar,
                channelTitle);
        Play play2 = playService.addPlayToPlaylist(
                playlist,
                member.getLoginId(),
                videoId,
                start,
                end,
                thumbnail,
                "play2",
                channelAvatar,
                channelTitle);
        Play play3 = playService.addPlayToPlaylist(
                playlist,
                member.getLoginId(),
                videoId,
                start,
                end,
                thumbnail,
                "play3",
                channelAvatar,
                channelTitle);
        List<PlaySeqDto> seqList = new ArrayList<>();
        seqList.add(PlaySeqDto.builder().id(play1.getId()).sequence(3).build());
        seqList.add(PlaySeqDto.builder().id(play2.getId()).sequence(1).build());
        seqList.add(PlaySeqDto.builder().id(play3.getId()).sequence(2).build());

        // when
        playService.editSeq(member.getLoginId(), playlist.getId(), seqList);

        // then
        assertAll(
                () -> assertThat(play1.getSequence()).isEqualTo(3),
                () -> assertThat(play2.getSequence()).isEqualTo(1),
                () -> assertThat(play3.getSequence()).isEqualTo(2)
        );
    }

    @Test
    void 영상_순서_수정_순서이상() {
        // given
        Member member = memberService.registerNon("device001", isPc);
        Playlist playlist = playlistService.createPlaylist("default", false, "OTHER", member);
        Play play1 = playService.addPlayToPlaylist(
                playlist,
                member.getLoginId(),
                videoId,
                start,
                end,
                thumbnail,
                "play1",
                channelAvatar,
                channelTitle);
        Play play2 = playService.addPlayToPlaylist(
                playlist,
                member.getLoginId(),
                videoId,
                start,
                end,
                thumbnail,
                "play2",
                channelAvatar,
                channelTitle);
        Play play3 = playService.addPlayToPlaylist(
                playlist,
                member.getLoginId(),
                videoId,
                start,
                end,
                thumbnail,
                "play3",
                channelAvatar,
                channelTitle);
        List<PlaySeqDto> seqList = new ArrayList<>();
        seqList.add(PlaySeqDto.builder().id(play1.getId()).sequence(3).build());
        seqList.add(PlaySeqDto.builder().id(play2.getId()).sequence(4).build());
        seqList.add(PlaySeqDto.builder().id(play3.getId()).sequence(2).build());

        // when
        Exception e = assertThrows(InvalidSeqException.class, () -> playService.editSeq(member.getLoginId(), playlist.getId(), seqList));

        // then
        assertThat(e.getMessage()).isEqualTo(InvalidSeqException.getErrorMessage());
    }

    @Test
    void 영상_순서_수정_중복() {
        // given
        Member member = memberService.registerNon("device001", isPc);
        Playlist playlist = playlistService.createPlaylist("default", false, "OTHER", member);
        Play play1 = playService.addPlayToPlaylist(
                playlist,
                member.getLoginId(),
                videoId,
                start,
                end,
                thumbnail,
                "play1",
                channelAvatar,
                channelTitle);
        Play play2 = playService.addPlayToPlaylist(
                playlist,
                member.getLoginId(),
                videoId,
                start,
                end,
                thumbnail,
                "play2",
                channelAvatar,
                channelTitle);
        Play play3 = playService.addPlayToPlaylist(
                playlist,
                member.getLoginId(),
                videoId,
                start,
                end,
                thumbnail,
                "play3",
                channelAvatar,
                channelTitle);
        List<PlaySeqDto> seqList = new ArrayList<>();
        seqList.add(PlaySeqDto.builder().id(play1.getId()).sequence(1).build());
        seqList.add(PlaySeqDto.builder().id(play2.getId()).sequence(2).build());
        seqList.add(PlaySeqDto.builder().id(play3.getId()).sequence(2).build());

        // when
        Exception e = assertThrows(DuplicateSeqException.class, () -> playService.editSeq(member.getLoginId(), playlist.getId(), seqList));

        // then
        assertThat(e.getMessage()).isEqualTo(DuplicateSeqException.getErrorMessage());
    }

    @Test
    void 영상_삭제() {
        // given
        Member member = memberService.registerNon("device001", isPc);
        Playlist playlist = playlistService.createPlaylist("default", false, "OTHER", member);
        Play play = playService.addPlayToPlaylist(
                playlist,
                member.getLoginId(),
                videoId,
                start,
                end,
                thumbnail,
                title,
                channelAvatar,
                channelTitle);

        // when
        playService.deletePlayById(play, "device001");
        Exception e = assertThrows(NotExistPlayException.class, () -> playService.getPlayById(play.getId()));

        // then
        assertThat(e.getMessage()).isEqualTo(NotExistPlayException.getErrorMessage());
    }

    @Test
    void 영상_재정렬() {
        // given
        Member member = memberService.registerNon("device001", isPc);
        Playlist playlist = playlistService.createPlaylist("default", false, "OTHER", member);
        Play play1 = playService.addPlayToPlaylist(
                playlist,
                member.getLoginId(),
                videoId,
                start,
                end,
                thumbnail,
                title,
                channelAvatar,
                channelTitle);

        Play play2 = playService.addPlayToPlaylist(
                playlist,
                member.getLoginId(),
                videoId,
                start,
                end,
                thumbnail,
                title,
                channelAvatar,
                channelTitle);
        play2.setSequence(3);

        // when
        playService.sortPlaysInPlaylist(playlist);

        // then
        List<Play> plays = playlist.getPlays();
        assertThat(play2.getSequence()).isEqualTo(2);
    }
}
