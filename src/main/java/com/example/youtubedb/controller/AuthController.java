package com.example.youtubedb.controller;

import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.dto.BaseResponseSuccessDto;
import com.example.youtubedb.dto.error.BadRequestFailResponseDto;
import com.example.youtubedb.dto.error.ServerErrorFailResponseDto;
import com.example.youtubedb.dto.member.request.MemberLoginRequestDto;
import com.example.youtubedb.dto.member.request.NonMemberCreateRequestDto;
import com.example.youtubedb.dto.member.response.MemberCreateResponseDto;
import com.example.youtubedb.domain.Token;
import com.example.youtubedb.dto.member.request.RealMemberCreateRequestDto;
import com.example.youtubedb.dto.token.request.TokenReissueRequestDto;
import com.example.youtubedb.dto.token.resposne.TokenResponseDto;
import com.example.youtubedb.service.AuthService;
import com.example.youtubedb.service.PlaylistService;
import com.example.youtubedb.util.RequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final PlaylistService playlistService;

    @PostMapping("/signup/real")
    public ResponseEntity<?> signupReal(@RequestBody RealMemberCreateRequestDto request) {
        RequestUtil.checkNeedValue(
                request.getLoginId(),
                request.getPassword(),
                request.getIsPc());

        Member realMember = authService.registerReal(request.getLoginId(), request.getPassword(), request.getIsPc());
        playlistService.createPlaylist("default", false, "OTHER", realMember);

        BaseResponseSuccessDto responseBody = new MemberCreateResponseDto(realMember);
        return ResponseEntity.ok(responseBody);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "비회원 생성 성공",
                    content = @Content(schema = @Schema(implementation = MemberCreateResponseDto.class))),
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
    @PostMapping("/signup/non")
    public ResponseEntity<?> signupNon(@RequestBody NonMemberCreateRequestDto request) {
        RequestUtil.checkNeedValue(
                request.getDeviceId(),
                request.getIsPc());
        Member nonMember = authService.registerNon(request.getDeviceId(), request.getIsPc());
        playlistService.createPlaylist("default", false, "OTHER", nonMember);

        BaseResponseSuccessDto responseBody = new MemberCreateResponseDto(nonMember);
        return ResponseEntity.ok(responseBody);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginRequestDto request) {
        RequestUtil.checkNeedValue(
                request.getLoginId(),
                request.getPassword());
        Member loginMember = authService.findMemberByLoginId(request.getLoginId());
        String password = loginMember.isMember() ? request.getPassword() : loginMember.getLoginId();
        Token token = authService.login(request.getLoginId(), password, loginMember.isPc());

        BaseResponseSuccessDto responseBody = new TokenResponseDto(token);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody TokenReissueRequestDto request) {
        RequestUtil.checkNeedValue(
                request.getAccessToken(),
                request.getRefreshToken());
//                request.getIsPc());
        Token token = authService.reissue(request.getAccessToken(), request.getRefreshToken(), request.getIsPc());

        BaseResponseSuccessDto responseBody = new TokenResponseDto(token);
        return ResponseEntity.ok(responseBody);
    }
}
