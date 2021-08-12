package com.example.youtubedb.controller;

import com.example.youtubedb.domain.Play;
import com.example.youtubedb.domain.Playlist;
import com.example.youtubedb.dto.BaseResponseSuccessDto;
import com.example.youtubedb.dto.error.BadRequestFailResponseDto;
import com.example.youtubedb.dto.error.NotAcceptableFailResponseDto;
import com.example.youtubedb.dto.error.ServerErrorFailResponseDto;
import com.example.youtubedb.dto.play.request.PlayCreateRequestDto;
import com.example.youtubedb.dto.play.request.PlayEditSeqRequestDto;
import com.example.youtubedb.dto.play.request.PlayEditTimeRequestDto;
import com.example.youtubedb.dto.play.response.*;
import com.example.youtubedb.service.PlayService;
import com.example.youtubedb.service.PlaylistService;
import com.example.youtubedb.util.RequestUtil;
import com.example.youtubedb.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// play, playlist의 변경 작업 시 본인인지 확인하는 부분을 aop로 빼도 괜찮을 듯?
// 근데 세부사항이 좀 다를 수 있어서..우선 Util로 빼놓자!
@Slf4j
@Tag(name = "영상 관련 API")
@RestController
@RequestMapping("/api/play")
public class PlayController {
    private final PlayService playService;
    private final PlaylistService playlistService;

    @Autowired
    public PlayController(PlayService playService,
                          PlaylistService playlistService) {
        this.playService = playService;
        this.playlistService = playlistService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = PlaysGetResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n " +
                            "1. 필요값 X\n" +
                            "2. 플레이 리스트 존재 X",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @GetMapping("/list/{playlistId}")
    @Operation(summary = "조회", description = "영상 목록 조회")
    public ResponseEntity<?> getPlays(@Parameter @PathVariable("playlistId") Long playlistId) {
        RequestUtil.checkNeedValue(playlistId);

        Playlist playlist = playlistService.getPlaylistById(playlistId);
        List<Play> plays = playService.getPlaysInPlaylist(playlist, "loginId?");

        BaseResponseSuccessDto responseBody = new PlaysGetResponseDto(plays);

        return ResponseEntity.ok(responseBody);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "생성 성공",
                    content = @Content(schema = @Schema(implementation = PlayCreateResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n " +
                            "1. 필요값 X\n" +
                            "2. 플레이 리스트 존재 X\n" +
                            "3. 시간값 바르지 않음",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @PostMapping("/create")
    @Operation(summary = "생성", description = "영상 생성")
    public ResponseEntity<?> createPlay(@RequestBody PlayCreateRequestDto playCreateRequestDto,
                                        Authentication authentication) {
        RequestUtil.checkNeedValue(
                playCreateRequestDto.getPlaylistId(),
                playCreateRequestDto.getVideoId(),
                playCreateRequestDto.getStart(),
                playCreateRequestDto.getEnd(),
                playCreateRequestDto.getThumbnail(),
                playCreateRequestDto.getTitle(),
                playCreateRequestDto.getChannelAvatar());

        log.info(" loginId = {}", authentication.getName());
        String loginId = authentication.getName();

        Playlist playlist = playlistService.getPlaylistById(playCreateRequestDto.getPlaylistId());
        Play play = playService.addPlayToPlaylist(
                playlist,
                loginId,
                playCreateRequestDto.getVideoId(),
                playCreateRequestDto.getStart(),
                playCreateRequestDto.getEnd(),
                playCreateRequestDto.getThumbnail(),
                playCreateRequestDto.getTitle(),
                playCreateRequestDto.getChannelAvatar());

        BaseResponseSuccessDto responseBody = new PlayCreateResponseDto(play);

        return ResponseEntity.ok(responseBody);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = PlayEditTimeResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n " +
                            "1. 필요값 X\n" +
                            "2. 영상 존재 X\n" +
                            "3. 시간값 바르지 않음",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @PutMapping("/edit/time")
    @Operation(summary = "재생 시간 변경", description = "영상 재생시간 변경")
    public ResponseEntity<?> editPlayTime(@RequestBody PlayEditTimeRequestDto playEditTimeRequestDto,
                                          Authentication authentication) {
        RequestUtil.checkNeedValue(
                playEditTimeRequestDto.getId(),
                playEditTimeRequestDto.getStart(),
                playEditTimeRequestDto.getEnd());

        log.info(" loginId = {}", authentication.getName());
        String loginId = authentication.getName();

        Play play = playService.getPlayById(playEditTimeRequestDto.getId());
        playService.editTime(
                play,
                loginId,
                playEditTimeRequestDto.getStart(),
                playEditTimeRequestDto.getEnd());

        BaseResponseSuccessDto responseBody = new PlayEditTimeResponseDto(playEditTimeRequestDto.getId());

        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/edit/seq")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = PlayEditSeqResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n " +
                            "1. 필요값 X\n" +
                            "2. 플레이 리스트 존재 X\n" +
                            "3. 순서가 바르지 않음(중복, 유효X)\n" +
                            "4. 영상 존재 X",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "406",
                    description = "올바르지 못한 접근",
                    content = @Content(schema = @Schema(implementation = NotAcceptableFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @Operation(summary = "순서 변경", description = "영상 재생순서 변경")
    public ResponseEntity<?> editPlaySequence(@RequestBody PlayEditSeqRequestDto playEditSeqRequestDto,
                                              Authentication authentication) {
        RequestUtil.checkNeedValue(
                playEditSeqRequestDto.getPlaylistId(),
                playEditSeqRequestDto.getSeqList());

        log.info(" loginId = {}", authentication.getName());
        String loginId = authentication.getName();


        playService.editSeq(loginId, playEditSeqRequestDto.getPlaylistId(), playEditSeqRequestDto.getSeqList());
        List<Map<String, Object>> result = ResponseUtil.getEditPlaysResponse(playEditSeqRequestDto.getSeqList());

        BaseResponseSuccessDto responseBody = new PlayEditSeqResponseDto(playEditSeqRequestDto.getSeqList());

        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/delete/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "삭제 성공",
                    content = @Content(schema = @Schema(implementation = PlayDeleteResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n " +
                            "1. 필요값 X\n" +
                            "2. 영상 존재 X\n",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @Operation(summary = "삭제", description = "영상 삭제")
    public ResponseEntity<?> deletePlay(@Parameter @PathVariable("id") Long id,
                                        Authentication authentication) {
        RequestUtil.checkNeedValue(id);
        log.info(" loginId = {}", authentication.getName());
        String loginId = authentication.getName();

        Play play = playService.getPlayById(id);
        playService.deletePlayById(play, loginId);
        playService.sortPlaysInPlaylist(play.getPlaylist());
        BaseResponseSuccessDto responseBody = new PlayDeleteResponseDto(id);

        return ResponseEntity.ok(responseBody);
    }
}
