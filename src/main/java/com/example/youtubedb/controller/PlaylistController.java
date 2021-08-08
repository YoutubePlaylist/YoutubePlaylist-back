package com.example.youtubedb.controller;

import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.domain.Playlist;
import com.example.youtubedb.dto.BaseResponseSuccessDto;
import com.example.youtubedb.dto.error.BadRequestFailResponseDto;
import com.example.youtubedb.dto.error.ServerErrorFailResponseDto;
import com.example.youtubedb.dto.playlist.request.PlaylistCreateRequestDto;
import com.example.youtubedb.dto.playlist.request.PlaylistEditTitleRequestDto;
import com.example.youtubedb.dto.playlist.response.PlaylistCreateResponseDto;
import com.example.youtubedb.dto.playlist.response.PlaylistDeleteResponseDto;
import com.example.youtubedb.dto.playlist.response.PlaylistEditTitleResponseDto;
import com.example.youtubedb.dto.playlist.response.PlaylistGetResponseDto;
import com.example.youtubedb.service.AuthService;
import com.example.youtubedb.service.PlaylistService;
import com.example.youtubedb.util.RequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Spring Security로 JWT 적용시 변경 필요
@Tag(name = "플레이 리스트 관련 API")
@RestController
@RequestMapping("/api/playlist")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final AuthService authService;

    @Autowired
    public PlaylistController(PlaylistService playlistService, AuthService authService) {
        this.playlistService = playlistService;
        this.authService = authService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = PlaylistGetResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n " +
                            "1. 필요값 X\n" +
                            "2. 멤버 존재 X" ,
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @Operation(summary = "조회", description = "플레이 리스트들 조회")
    @GetMapping("/{loginId}")
    public ResponseEntity<?> getPlaylist(@Parameter @PathVariable("loginId") String loginId) {
        Member member = authService.findMemberByLoginId(loginId);
        List<Playlist> playlists = member.getPlaylists();
        playlistService.addThumbnail(playlists);

        BaseResponseSuccessDto responseBody = new PlaylistGetResponseDto(playlists);

        return ResponseEntity.ok(responseBody);
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "생성 성공",
                    content = @Content(schema = @Schema(implementation = PlaylistCreateResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n " +
                            "1. 필요값 X\n" +
                            "2. 멤버 존재 X\n" +
                            "3. 카테고리 존재 X" ,
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @PostMapping("/create")
    @Operation(summary = "생성", description = "플레이 리스트 생성")
    public ResponseEntity<?> createPlaylist(@RequestBody PlaylistCreateRequestDto request) { // 여긴 임시로 loginId 필요
        RequestUtil.checkNeedValue(
                request.getLoginId(),
                request.getTitle(),
                request.getIsPublic(),
                request.getCategory());
        Member member = authService.findMemberByLoginId(request.getLoginId());
        Playlist playlist = playlistService.createPlaylist(
                request.getTitle(),
                request.getIsPublic(),
                request.getCategory(),
                member);

        BaseResponseSuccessDto responseBody = new PlaylistCreateResponseDto(playlist);

        return ResponseEntity.ok(responseBody);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = PlaylistEditTitleResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n " +
                            "1. 필요값 X\n" +
                            "2. 멤버 존재 X\n" +
                            "3. 플레이 리스트 존재 X" ,
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @PutMapping("/edit")
    @Operation(summary = "제목 수정", description = "플레이 리스트 제목 수정")
    public ResponseEntity<?> editPlaylist(@RequestBody PlaylistEditTitleRequestDto request) {
        RequestUtil.checkNeedValue(
                request.getId(),
                request.getTitle());
        playlistService.editPlaylistTitle(request.getId(), request.getTitle(), "loginId");

        BaseResponseSuccessDto responseBody = new PlaylistEditTitleResponseDto(request.getId());

        return ResponseEntity.ok(responseBody);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "삭제 성공",
                    content = @Content(schema = @Schema(implementation = PlaylistDeleteResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n " +
                            "1. 필요값 X\n" +
                            "2. 플레이 리스트 존재 X" ,
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "삭제", description = "플레이 리스트 삭제")
    public ResponseEntity<?> deletePlaylist(@Parameter @PathVariable("id") Long id) {
        RequestUtil.checkNeedValue(id);
        playlistService.deletePlaylistById(id, "loginId?");

        BaseResponseSuccessDto responseBody = new PlaylistDeleteResponseDto(id);

        return ResponseEntity.ok(responseBody);
    }
}
