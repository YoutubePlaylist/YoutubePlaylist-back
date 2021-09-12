package com.example.youtubedb.controller;

import com.example.youtubedb.domain.Playlist;
import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.dto.BaseResponseSuccessDto;
import com.example.youtubedb.dto.error.BadRequestFailResponseDto;
import com.example.youtubedb.dto.error.ServerErrorFailResponseDto;
import com.example.youtubedb.dto.playlist.request.PlaylistCreateRequestDto;
import com.example.youtubedb.dto.playlist.request.PlaylistEditTitleRequestDto;
import com.example.youtubedb.dto.playlist.response.PlaylistCreateResponseDto;
import com.example.youtubedb.dto.playlist.response.PlaylistDeleteResponseDto;
import com.example.youtubedb.dto.playlist.response.PlaylistEditTitleResponseDto;
import com.example.youtubedb.dto.playlist.response.PlaylistGetResponseDto;
import com.example.youtubedb.service.MemberService;
import com.example.youtubedb.service.PlaylistService;
import com.example.youtubedb.util.RequestUtil;
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

import javax.validation.Valid;
import java.util.List;

// TODO: 현재 중복된 플레이 리스트 이름 가능한데 논의 해봐야할듯
@Slf4j
@Tag(name = "플레이 리스트 관련 API")
@RestController
@RequestMapping("/api/playlist")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final MemberService memberService;

    @Autowired
    public PlaylistController(PlaylistService playlistService,
                              MemberService memberService) {
        this.playlistService = playlistService;
        this.memberService = memberService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = PlaylistGetResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n " +
                            "1. 필요값 X\n" +
                            "2. 멤버 존재 X",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @Operation(summary = "조회", description = "플레이 리스트들 조회")
    @GetMapping
    public ResponseEntity<?> getPlaylist(Authentication authentication) {
        log.info(" loginId = {}", authentication.getName());
        String loginId = authentication.getName();
        Member member = memberService.findMemberByLoginId(loginId);
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
                            "3. 카테고리 존재 X",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @PostMapping("/create")
    @Operation(summary = "생성", description = "플레이 리스트 생성")
    public ResponseEntity<?> createPlaylist(@Valid  @RequestBody PlaylistCreateRequestDto playlistCreateRequestDto,
                                            Authentication authentication) {
        RequestUtil.checkNeedValue(
                playlistCreateRequestDto.getTitle(),
                playlistCreateRequestDto.getIsPublic(),
                playlistCreateRequestDto.getCategory());

        String loginId = authentication.getName();
        Member member = memberService.findMemberByLoginId(loginId);
        List<Playlist> playlists = member.getPlaylists();


        Playlist playlist = playlistService.createPlaylist(
                playlistCreateRequestDto.getTitle(),
                playlistCreateRequestDto.getIsPublic(),
                playlistCreateRequestDto.getCategory(),
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
                            "3. 플레이 리스트 존재 X",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @PutMapping("/edit")
    @Operation(summary = "제목 수정", description = "플레이 리스트 제목 수정")
    public ResponseEntity<?> editPlaylist(@Valid @RequestBody PlaylistEditTitleRequestDto playlistEditTitleRequestDto,
                                          Authentication authentication) {
        RequestUtil.checkNeedValue(
                playlistEditTitleRequestDto.getId(),
                playlistEditTitleRequestDto.getTitle());

        log.info(" loginId = {}", authentication.getName());
        String loginId = authentication.getName();

        playlistService.editPlaylistTitle(playlistEditTitleRequestDto.getId(), playlistEditTitleRequestDto.getTitle(), loginId);

        BaseResponseSuccessDto responseBody = new PlaylistEditTitleResponseDto(playlistEditTitleRequestDto.getId());

        return ResponseEntity.ok(responseBody);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "삭제 성공",
                    content = @Content(schema = @Schema(implementation = PlaylistDeleteResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n " +
                            "1. 필요값 X\n" +
                            "2. 플레이 리스트 존재 X",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "삭제", description = "플레이 리스트 삭제")
    public ResponseEntity<?> deletePlaylist(@Parameter @PathVariable("id") Long id,
                                            Authentication authentication) {
        RequestUtil.checkNeedValue(id);

        log.info(" loginId = {}", authentication.getName());
        String loginId = authentication.getName();

        playlistService.deletePlaylistById(id, loginId);

        BaseResponseSuccessDto responseBody = new PlaylistDeleteResponseDto(id);

        return ResponseEntity.ok(responseBody);
    }
}
