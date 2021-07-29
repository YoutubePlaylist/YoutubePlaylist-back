package com.example.youtubedb.controller;

import com.example.youtubedb.domain.Play;
import com.example.youtubedb.domain.Playlist;
import com.example.youtubedb.dto.ResponseDto;
import com.example.youtubedb.dto.play.PlayEditSeqRequestDto;
import com.example.youtubedb.service.MemberService;
import com.example.youtubedb.service.PlayService;
import com.example.youtubedb.service.PlaylistService;
import com.example.youtubedb.util.RequestUtil;
import com.example.youtubedb.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// play, playlist의 변경 작업 시 본인인지 확인하는 부분을 aop로 빼도 괜찮을 듯?
// 근데 세부사항이 좀 다를 수 있어서..우선 Util로 빼놓자!

@RestController
@Tag(name = "영상 관련 API")
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
    @Operation(description = "영상 목록 조회")
    public ResponseEntity<?> getPlays(@RequestBody Map<String, String> request) {
        RequestUtil.checkNeedValue(request.get("loginId"), request.get("playlistId"));

        Playlist playlist = playlistService.getPlaylistById(Long.parseLong(request.get("playlistId")));
        List<Play> plays = playService.getPlaysInPlaylist(playlist, request.get("loginId"));

        ResponseDto responseBody = ResponseUtil.getSuccessResponse(plays);

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/create")
    @Operation(description = "영상 생성")
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

        Playlist playlist = playlistService.getPlaylistById(Long.parseLong(request.get("playlistId")));
        Play play = playService.addPlayToPlaylist(
                playlist,
                request.get("loginId"),
                request.get("videoId"),
                Long.parseLong(request.get("start")),
                Long.parseLong(request.get("end")),
                request.get("thumbnail"),
                request.get("title"),
                request.get("channelAvatar"));

        ResponseDto responseBody = ResponseUtil.getSuccessResponse(play);

        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/edit/time")
    @Operation(description = "영상 재생시간 변경")
    public ResponseEntity<?> editPlayTime(@RequestBody Map<String, String> request) {
        RequestUtil.checkNeedValue(
                request.get("loginId"),
                request.get("id"),
                request.get("start"),
                request.get("end"));

        Long lId = Long.parseLong(request.get("id"));
        Play play = playService.getPlayById(lId);
        playService.editTime(
                play,
                request.get("loginId"),
                Long.parseLong(request.get("start")),
                Long.parseLong(request.get("end")));

        ResponseDto responseBody = ResponseUtil.getSuccessResponse(ResponseUtil.getEditResponse(request.get("id")));

        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/edit/seq")
    @Operation(description = "영상 재생순서 변경")
    public ResponseEntity<?> editPlaySequence(@RequestBody PlayEditSeqRequestDto request) {
        RequestUtil.checkNeedValue(
                request.getLoginId(),
                request.getPlaylistId(),
                request.getSeqList());

        playService.editSeq(request.getLoginId(), request.getPlaylistId(), request.getSeqList());
//        List<Object> result = request.getSeqList().stream().map(p -> ResponseUtil.getEditResponse(p.getId())).collect(Collectors.toList());
        List<Map<String, Object>> result = ResponseUtil.getEditPlaysResponse(request.getSeqList());
        ResponseDto responseBody = ResponseUtil.getSuccessResponse(result);

        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/delete")
    @Operation(description = "영상 삭제")
    public ResponseEntity<?> deletePlay(@RequestBody Map<String, String> request) {
        RequestUtil.checkNeedValue(request.get("loginId"), request.get("id"));

        Long lId = Long.parseLong(request.get("id"));
        Play play = playService.getPlayById(lId);
        playService.deletePlayById(play, request.get("loginId"));

        ResponseDto responseBody = ResponseUtil.getSuccessResponse(ResponseUtil.getDeleteResponse(request.get("id")));

        return ResponseEntity.ok(responseBody);
    }
}
