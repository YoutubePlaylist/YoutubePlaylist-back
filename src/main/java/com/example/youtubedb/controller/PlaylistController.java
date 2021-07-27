package com.example.youtubedb.controller;

import com.example.youtubedb.domain.Member;
import com.example.youtubedb.domain.Playlist;
import com.example.youtubedb.dto.ResponseDto;
import com.example.youtubedb.service.MemberService;
import com.example.youtubedb.service.PlaylistService;
import com.example.youtubedb.util.ResponseUtil;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Spring Security로 JWT 적용시 변경 필요
@RestController
@RequestMapping("/api/playlist")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final MemberService memberService;

    @Autowired
    public PlaylistController(PlaylistService playlistService, MemberService memberService) {
        this.playlistService = playlistService;
        this.memberService = memberService;
    }

    @GetMapping("/{loginId}")
    public ResponseEntity<?> getPlaylist(@PathVariable("loginId") String loginId) {
        Member member = memberService.findMemberByLoginId(loginId);
        List<Playlist> playlists = member.getPlaylists();

        ResponseDto responseBody = ResponseUtil.getSuccessResponse(playlists);

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPlaylist(@RequestBody Map<String, String> request) {
        Member member = memberService.findMemberByLoginId(request.get("loginId"));
        Playlist playlist = playlistService.createPlaylist(
                request.get("title"),
                request.get("public"),
                request.get("category"),
                member);

        ResponseDto responseBody = ResponseUtil.getSuccessResponse(playlist);

        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editPlaylist(@RequestBody Map<String, String> request) {
        Member member = memberService.findMemberByLoginId(request.get("loginId"));
        playlistService.editPlaylistTitle(request.get("id"), request.get("title"), member);

        ResponseDto responseBody = ResponseUtil.getSuccessResponse(ResponseUtil.getEditResponse(request.get("id")));

        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePlaylist(@RequestBody Map<String, String> request) {
        Member member = memberService.findMemberByLoginId(request.get("loginId"));
        playlistService.deletePlaylistById(request.get("id"), member);

        ResponseDto responseBody = ResponseUtil.getSuccessResponse(ResponseUtil.getDeleteResponse(request.get("id")));

        return ResponseEntity.ok(responseBody);
    }

}
