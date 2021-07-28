package com.example.youtubedb.controller;

import com.example.youtubedb.domain.Member;
import com.example.youtubedb.domain.Play;
import com.example.youtubedb.domain.Playlist;
import com.example.youtubedb.dto.ResponseDto;
import com.example.youtubedb.service.MemberService;
import com.example.youtubedb.service.PlayService;
import com.example.youtubedb.service.PlaylistService;
import com.example.youtubedb.util.RequestUtil;
import com.example.youtubedb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// play, playlist의 변경 작업 시 본인인지 확인하는 부분을 aop로 빼도 괜찮을 듯?

@RestController
@RequestMapping("/api/play")
public class PlayController {
    private final PlayService playService;
    private final PlaylistService playlistService;
    private final MemberService memberService;

    @Autowired
    public PlayController(PlayService playService, PlaylistService playlistService, MemberService memberService) {
        this.playService = playService;
        this.playlistService = playlistService;
        this.memberService = memberService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getPlays(@RequestBody Map<String, String> request) {
        RequestUtil.checkNeedValue(request.get("loginId"), request.get("playlistId"));

        Member member = memberService.findMemberByLoginId(request.get("loginId"));
        Playlist playlist = playlistService.checkExistPlaylist(Long.parseLong(request.get("playlistId")));
        List<Play> plays = playService.getPlaysInPlaylist(playlist, member);

        ResponseDto responseBody = ResponseUtil.getSuccessResponse(plays);

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPlay(@RequestBody Map<String, String> request) {
        RequestUtil.checkNeedValue(
                request.get("loginId"),
                request.get("playlistId"),
                request.get("videoId"),
                request.get("start"),
                request.get("end"),
                request.get("thumbnail"),
                request.get("title"),
                request.get("channelAvatar"));

        Member member = memberService.findMemberByLoginId(request.get("loginId"));
        Playlist playlist = playlistService.checkExistPlaylist(Long.parseLong(request.get("playlistId")));
        playlistService.checkOwn(playlist, member);
        Play play = playService.addPlayToPlaylist(
                playlist,
                request.get("videoId"),
                Long.parseLong(request.get("start")),
                Long.parseLong(request.get("end")),
                request.get("thumbnail"),
                request.get("title"),
                request.get("channelAvatar"));

        ResponseDto responseBody = ResponseUtil.getSuccessResponse(play);

        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/delete")
}
