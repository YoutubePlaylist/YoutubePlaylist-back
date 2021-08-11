package com.example.youtubedb.controller;

import com.example.youtubedb.config.jwt.TokenProvider;
import com.example.youtubedb.domain.Token;
import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.dto.BaseResponseSuccessDto;
import com.example.youtubedb.dto.error.BadRequestFailResponseDto;
import com.example.youtubedb.dto.error.ServerErrorFailResponseDto;
import com.example.youtubedb.dto.member.request.MemberRequestDto;
import com.example.youtubedb.dto.member.request.NonMemberRequestDto;
import com.example.youtubedb.dto.member.response.MemberResponseDto;
import com.example.youtubedb.dto.member.response.NonMemberResponseDto;
import com.example.youtubedb.dto.token.request.TokenReissueRequestDto;
import com.example.youtubedb.dto.token.resposne.TokenResponseDto;
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

// TODO actuator 관련 이슈 해결 필요!
// TODO 404에러 관리할 수 있으면 좋을듯

@Tag(name = "회원 관련 API")
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final PlaylistService playlistService;
    private final TokenProvider tokenProvider;

    @Autowired
    public MemberController(MemberService memberService,
                            PlaylistService playlistService,
                            TokenProvider tokenProvider) {
        this.memberService = memberService;
        this.playlistService = playlistService;
        this.tokenProvider = tokenProvider;
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
    @PostMapping("/signup/non")
    public ResponseEntity<?> signupNon(@RequestBody NonMemberRequestDto nonMemberRequestDto) {
        RequestUtil.checkNeedValue(
                nonMemberRequestDto.getDeviceId(),
                nonMemberRequestDto.getIsPC());

        Member nonMember = memberService.registerNon(nonMemberRequestDto.getDeviceId(), nonMemberRequestDto.getIsPC());
        playlistService.createPlaylist("default", false, "OTHER", nonMember);

        BaseResponseSuccessDto responseBody = new NonMemberResponseDto(nonMember);
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
    @PostMapping("/signup/real")
    public ResponseEntity<?> signupReal(@RequestBody MemberRequestDto memberRequestDto) {
        RequestUtil.checkNeedValue(
                memberRequestDto.getLoginId(),
                memberRequestDto.getPassword(),
                memberRequestDto.getIsPC());

        Member member = memberService.registerReal(memberRequestDto.getLoginId(), memberRequestDto.getPassword(), memberRequestDto.getIsPC());
        playlistService.createPlaylist("default", false, "OTHER", member);

        BaseResponseSuccessDto responseBody = new MemberResponseDto(member);
        return ResponseEntity.ok(responseBody);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = TokenResponseDto.class))),
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
    public ResponseEntity<?> login(@RequestBody MemberRequestDto memberRequestDto) {
        RequestUtil.checkNeedValue(
                memberRequestDto.getLoginId(),
                memberRequestDto.getIsPC());

        Member member = memberService.findMemberByLoginId(memberRequestDto.getLoginId());
        String password = member.isMember() ? memberRequestDto.getPassword() : member.getLoginId();
        Token token = memberService.login(memberRequestDto.getLoginId(), password, memberRequestDto.getIsPC());

        BaseResponseSuccessDto responseBody = new TokenResponseDto(token);
        return ResponseEntity.ok(responseBody);
    }

    //    @DeleteMapping("/delete")
//    public ResponseEntity<?> deleteMember(HttpServletRequest request) {
////        String token = token.resolveToken(request);
//        String loginId = tokenProvider.resolveToken(token);
//        memberService.deleteUserByLoginId(loginId);
//        jwtTokenProvider.abandonToken(token);
//
//        return ResponseEntity.ok(null);
//    }
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody TokenReissueRequestDto reissueRequestDto) {
        RequestUtil.checkNeedValue(
                reissueRequestDto.getAccessToken(),
                reissueRequestDto.getRefreshToken(),
                reissueRequestDto.getIsPC());
        Token token = memberService.reissue(reissueRequestDto.getAccessToken(), reissueRequestDto.getRefreshToken(), reissueRequestDto.getIsPC());

        BaseResponseSuccessDto responseBody = new TokenResponseDto(token);
        return ResponseEntity.ok(responseBody);
    }
}
