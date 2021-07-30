package com.example.youtubedb.controller;

import com.example.youtubedb.domain.Member;
import com.example.youtubedb.dto.BaseResponseSuccessDto;
import com.example.youtubedb.dto.error.BadRequestFailResponseDto;
import com.example.youtubedb.dto.error.ServerErrorFailResponseDto;
import com.example.youtubedb.dto.member.request.NonMemberCreateRequestDto;
import com.example.youtubedb.dto.member.response.NonMemberCreateResponseDto;
import com.example.youtubedb.service.MemberService;
import com.example.youtubedb.service.PlaylistService;
import com.example.youtubedb.util.RequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 관련 API")
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final PlaylistService playlistService;

    @Autowired
    public MemberController(MemberService memberService, PlaylistService playlistService) {
        this.memberService = memberService;
        this.playlistService = playlistService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "비회원 생성 성공",
                    content = @Content(schema = @Schema(implementation = NonMemberCreateResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n" +
                            "1. 중복된 아이디 존재\n" +
                            "2. 필요값 X",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @Operation(summary = "가입", description = "비회원 가입")
    @PostMapping("/register/non")
    public ResponseEntity<?> registerNonMember(@RequestBody NonMemberCreateRequestDto request) {
        RequestUtil.checkNeedValue(request.getDeviceId());
        Member nonMember = memberService.registerNon(request.getDeviceId());
        playlistService.createPlaylist("default", false, "OTHER", nonMember);

        BaseResponseSuccessDto responseBody = new NonMemberCreateResponseDto(nonMember);

        return ResponseEntity.ok(responseBody);
    }
}
