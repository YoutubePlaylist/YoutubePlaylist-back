package com.example.youtubedb.controller;

import com.example.youtubedb.domain.Token;
import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.dto.BaseResponseSuccessDto;
import com.example.youtubedb.dto.error.AuthenticationEntryPointFailResponseDto;
import com.example.youtubedb.dto.error.BadRequestFailResponseDto;
import com.example.youtubedb.dto.error.ServerErrorFailResponseDto;
import com.example.youtubedb.dto.member.request.MemberChangePasswordRequestDto;
import com.example.youtubedb.dto.member.request.MemberLoginRequestDto;
import com.example.youtubedb.dto.member.request.MemberRequestDto;
import com.example.youtubedb.dto.member.request.NonMemberRequestDto;
import com.example.youtubedb.dto.member.response.MemberDeleteResponseDto;
import com.example.youtubedb.dto.member.response.MemberResponseDto;
import com.example.youtubedb.dto.member.response.NonMemberResponseDto;
import com.example.youtubedb.dto.token.request.TokenReissueRequestDto;
import com.example.youtubedb.dto.token.resposne.TokenResponseDto;
import com.example.youtubedb.s3.S3Uploader;
import com.example.youtubedb.service.MemberService;
import com.example.youtubedb.service.MessageService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// TODO actuator 관련 이슈 해결 필요!

@Tag(name = "회원 관련 API")
@RestController
@RequestMapping("/api/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final PlaylistService playlistService;
    private final S3Uploader s3Uploader;

    @Autowired
    public MemberController(MemberService memberService,
                            PlaylistService playlistService,
                            S3Uploader s3Uploader) {
        this.memberService = memberService;
        this.playlistService = playlistService;
        this.s3Uploader = s3Uploader;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n" +
                            "1. 아이디 존재 X",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "인증되지 않음",
                    content = @Content(schema = @Schema(implementation = AuthenticationEntryPointFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @Operation(summary = "조회", description = "회원&비회원 정보 조회")
    @GetMapping
    @ResponseBody
    public ResponseEntity<?> getMemberInfo(Authentication authentication) {
        log.info(" loginId = {}", authentication.getName());
        String loginId = authentication.getName();
        Member member = memberService.findMemberByLoginId(loginId);

        BaseResponseSuccessDto responseBody = new MemberResponseDto(member);
        return ResponseEntity.ok(responseBody);
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
                    description = "* 서버 에러",
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
                            "2. 필요값 X\n" +
                            "3. 비밀번호 생성규칙 X",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "* 서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @Operation(summary = "가입", description = "회원 가입")
    @PostMapping("/signup/real")
    public ResponseEntity<?> signupReal(@RequestBody MemberRequestDto memberRequestDto) {
        RequestUtil.checkNeedValue(
                memberRequestDto.getLoginId(),
                memberRequestDto.getPassword(),
                memberRequestDto.getIsPC());

//        log.info("sms test 중, checkpoint:1");
//        messageService.sendMessage("01086231917","1234");
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
                    description = "* 서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @Operation(summary = "로그인(회원+비회원)", description = "비회원+회원 로그인")
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "회원 삭제 성공",
                    content = @Content(schema = @Schema(implementation = MemberDeleteResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n" +
                            "1. 아이디 존재 X",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "* 서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @Operation(summary = "회원 삭제", description = "회원 삭제")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMember(Authentication authentication) {
        log.info(" authentication = {}", authentication);
        log.info(" loginId = {}", authentication.getName());
        String loginId = authentication.getName();
        memberService.deleteUserByLoginId(loginId);

        BaseResponseSuccessDto responseBody = new MemberDeleteResponseDto(loginId);
        return ResponseEntity.ok(responseBody);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "토큰 재발급 성공",
                    content = @Content(schema = @Schema(implementation = TokenResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n" +
                            "1. refresh 토큰 유효X\n" +
                            "2. refresh 토큰 기간 만료\n" +
                            "3. refresh 토큰 불일치\n" +
                            "4. 필요값 X",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "* 서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @Operation(summary = "토큰 재발급", description = "access토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody TokenReissueRequestDto reissueRequestDto) throws Exception {
        RequestUtil.checkNeedValue(
                reissueRequestDto.getAccessToken(),
                reissueRequestDto.getRefreshToken(),
                reissueRequestDto.getIsPC());
        Token token = memberService.reissue(reissueRequestDto.getAccessToken(), reissueRequestDto.getRefreshToken(), reissueRequestDto.getIsPC());

        BaseResponseSuccessDto responseBody = new TokenResponseDto(token);
        return ResponseEntity.ok(responseBody);
    }

    // TODO: 비밀번호 변경
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "비밀 번호 변경 성공",
                    content = @Content(schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n" +
                            "1. 비밀번호가 일치하지 않습니다.\n" +
                            "2. 기존 비밀번호와 같은 비밀번호 입니다.\n" +
                            "3. 필요값 X",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "* 서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @Operation(summary = "비밀 번호 변경", description = "비밀 번호 변경")
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody MemberChangePasswordRequestDto memberChangePasswordRequestDto,
                                            Authentication authentication) {
        String loginId = authentication.getName();
        RequestUtil.checkNeedValue(
                memberChangePasswordRequestDto.getOldPassword(),
                memberChangePasswordRequestDto.getNewPassword());

        Member updateMember = memberService.findMemberByLoginId(loginId);
        updateMember = memberService.changePassword(updateMember, memberChangePasswordRequestDto.getOldPassword(),memberChangePasswordRequestDto.getNewPassword());

        BaseResponseSuccessDto responseBody = new MemberResponseDto(updateMember);
        return ResponseEntity.ok(responseBody);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "프로필 이미지 업로드",
                    content = @Content(schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n" +
                            "1. 아이디 존재 X\n" +
                            "2. 회원이 아닌 비회원일 때\n" +
                            "3. 필요값 X",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "인증되지 않음",
                    content = @Content(schema = @Schema(implementation = AuthenticationEntryPointFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "* 서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @Operation(summary = "프로필 이미지 업로드", description = "프로필 이미지 업로드 & 수정")
    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public ResponseEntity<?> upload(
            @Parameter(
                    description = "프로필 이미지 파일"
            )
            @RequestParam("img") MultipartFile img,
            Authentication authentication) throws IOException {
        RequestUtil.checkNeedValue(img);
        log.info(" loginId = {}", authentication.getName());
        String loginId = authentication.getName();
        Member member = memberService.findMemberByLoginId(loginId);
        memberService.checkMember(member);
        String profileImg = s3Uploader.upload(img, "static");
        memberService.setProfileImg(member, profileImg);

        BaseResponseSuccessDto responseBody = new MemberResponseDto(member);
        return ResponseEntity.ok(responseBody);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "회원으로 변경",
                    content = @Content(schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "* 잘못된 요청\n" +
                            "1. 아이디 존재 X\n" +
                            "2. 필요값 X\n" +
                            "3. 비밀번호 생성규칙 X",
                    content = @Content(schema = @Schema(implementation = BadRequestFailResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "인증되지 않음",
                    content = @Content(schema = @Schema(implementation = AuthenticationEntryPointFailResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "서버 에러",
                    content = @Content(schema = @Schema(implementation = ServerErrorFailResponseDto.class)))
    })
    @Operation(summary = "회원으로 변경", description = "비회원에서 회원으로 변경")
    @PutMapping("/change")
    @ResponseBody
    public ResponseEntity<?> changeToMember(Authentication authentication,
                                            @RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        RequestUtil.checkNeedValue(memberLoginRequestDto.getLoginId(), memberLoginRequestDto.getPassword());
        log.info(" loginId = {}", authentication.getName());
        String loginId = authentication.getName();
        Member member = memberService.findMemberByLoginId(loginId);
        memberService.change(member, memberLoginRequestDto.getLoginId(), memberLoginRequestDto.getPassword());

        BaseResponseSuccessDto responseBody = new MemberResponseDto(member);
        return ResponseEntity.ok(responseBody);
    }
}
