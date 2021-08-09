package com.example.youtubedb.controller;

import com.example.youtubedb.auth.JwtTokenProvider;
import com.example.youtubedb.domain.Member;
import com.example.youtubedb.dto.BaseResponseSuccessDto;
import com.example.youtubedb.dto.error.BadRequestFailResponseDto;
import com.example.youtubedb.dto.error.ServerErrorFailResponseDto;
import com.example.youtubedb.dto.member.request.MemberRequestDto;
import com.example.youtubedb.dto.member.request.NonMemberRequestDto;
import com.example.youtubedb.dto.member.response.MemberResponseDto;
import com.example.youtubedb.dto.member.response.NonMemberResponseDto;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

// TODO actuator 관련 이슈 해결 필요!
// TODO 404에러 관리할 수 있으면 좋을듯

@Tag(name = "회원 관련 API")
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final PlaylistService playlistService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MemberController(MemberService memberService,
                            PlaylistService playlistService,
                            JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.playlistService = playlistService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "비회원 생성 성공",
                    content = @Content(schema = @Schema(implementation = NonMemberResponseDto.class))),
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
    public ResponseEntity<?> registerNonMember(@RequestBody NonMemberRequestDto nonMemberRequestDto) {
        RequestUtil.checkNeedValue(nonMemberRequestDto.getDeviceId(), nonMemberRequestDto.getIsPC());
        Member nonMember = memberService.registerNon(nonMemberRequestDto.getDeviceId());
        playlistService.createPlaylist("default", false, "OTHER", nonMember);

        BaseResponseSuccessDto responseBody = new NonMemberResponseDto(
                nonMember,
                jwtTokenProvider.createToken(nonMember.getLoginId(), nonMember.getRole(), nonMemberRequestDto.getIsPC()));

        return ResponseEntity.ok(responseBody);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "비회원 로그인 성공",
                    content = @Content(schema = @Schema(implementation = NonMemberResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n" +
                            "1. 아이디 존재 X\n" +
                            "2. 필요값 X",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @Operation(summary = "로그인", description = "비회원 로그인")
    @PostMapping("/login/non")
    public ResponseEntity<?> loginNonMember(@RequestBody NonMemberRequestDto nonMemberRequestDto) {
        RequestUtil.checkNeedValue(
                nonMemberRequestDto.getDeviceId(),
                nonMemberRequestDto.getIsPC());

        Member nonMember = memberService.findMemberByLoginId(nonMemberRequestDto.getDeviceId());

        BaseResponseSuccessDto responseBody = new MemberResponseDto(
                nonMember,
                jwtTokenProvider.createToken(nonMember.getLoginId(), nonMember.getRole(), nonMemberRequestDto.getIsPC()));

        return ResponseEntity.ok(responseBody);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "회원 생성 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n" +
                            "1. 중복된 아이디 존재\n" +
                            "2. 필요값 X",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @Operation(summary = "가입", description = "회원 가입")
    @PostMapping("/register")
    public ResponseEntity<?> registerMember(@RequestBody MemberRequestDto memberRequestDto) {
        RequestUtil.checkNeedValue(
                memberRequestDto.getLoginId(),
                memberRequestDto.getPassword(),
                memberRequestDto.getIsPC());
        Member member = memberService.register(memberRequestDto.getLoginId(), memberRequestDto.getPassword());
        playlistService.createPlaylist("default", false, "OTHER", member);

        BaseResponseSuccessDto responseBody = new MemberResponseDto(
                member,
                jwtTokenProvider.createToken(member.getLoginId(), member.getRole(), memberRequestDto.getIsPC()));

        return ResponseEntity.ok(responseBody);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n" +
                            "1. 아이디 존재 X\n" +
                            "2. 비밀번호 틀림 \n" +
                            "3. 필요값 X",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @Operation(summary = "로그인", description = "회원 로그인")
    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@RequestBody MemberRequestDto memberRequestDto) {
        RequestUtil.checkNeedValue(
                memberRequestDto.getLoginId(),
                memberRequestDto.getPassword(),
                memberRequestDto.getIsPC());
        Member member = memberService.login(memberRequestDto.getLoginId(), memberRequestDto.getPassword());

        BaseResponseSuccessDto responseBody = new MemberResponseDto(
                member,
                jwtTokenProvider.createToken(member.getLoginId(), member.getRole(), memberRequestDto.getIsPC()));

        return ResponseEntity.ok(responseBody);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMember(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String loginId = jwtTokenProvider.getUserPk(token);
        memberService.deleteUserByLoginId(loginId);
        jwtTokenProvider.abandonToken(token);

        return ResponseEntity.ok(null);
    }
}
