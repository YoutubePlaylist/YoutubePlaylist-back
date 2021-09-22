package com.example.youtubedb.controller;

import static com.example.youtubedb.Contracts.requires;
import static com.example.youtubedb.dto.member.request.MemberChangePasswordRequestProvider.FailedReason.NOT_FOUND_MEMBER;
import static com.example.youtubedb.dto.member.request.MemberChangePasswordRequestProvider.FailedReason.NOT_MATCHED_OLD_PASSWORD;
import static com.example.youtubedb.dto.member.request.MemberChangePasswordRequestProvider.FailedReason.UNEXPECTED_NEW_PASSWORD;
import static com.example.youtubedb.dto.member.request.MemberChangePasswordRequestProvider.FailedReason.values;
import static java.util.stream.Collectors.joining;

import com.example.youtubedb.Contracts;
import com.example.youtubedb.domain.Token;
import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.dto.BaseResponseSuccessDto;
import com.example.youtubedb.dto.error.AuthenticationEntryPointFailResponseDto;
import com.example.youtubedb.dto.error.BadRequestFailResponseDto;
import com.example.youtubedb.dto.error.ServerErrorFailResponseDto;
import com.example.youtubedb.dto.member.request.MemberChangePasswordRequestDto;
import com.example.youtubedb.dto.member.request.MemberChangePasswordRequestProvider;
import com.example.youtubedb.dto.member.request.MemberChangePasswordRequestProvider.FailedReason;
import com.example.youtubedb.dto.member.request.MemberChangePasswordRequestProvider.Result;
import com.example.youtubedb.dto.member.request.MemberLoginRequestDto;
import com.example.youtubedb.dto.member.request.MemberRequestDto;
import com.example.youtubedb.dto.member.request.NonMemberRequestDto;
import com.example.youtubedb.dto.member.response.MemberDeleteResponseDto;
import com.example.youtubedb.dto.member.response.MemberResponseDto;
import com.example.youtubedb.dto.member.response.NonMemberResponseDto;
import com.example.youtubedb.dto.token.request.TokenReissueRequestDto;
import com.example.youtubedb.dto.token.resposne.TokenResponseDto;
import com.example.youtubedb.s3.S3Uploader;
import com.example.youtubedb.service.ChangingPassword;
import com.example.youtubedb.service.MemberService;
import com.example.youtubedb.service.PasswordValidationService;
import com.example.youtubedb.service.PlaylistService;
import com.example.youtubedb.util.RequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// TODO actuator 관련 이슈 해결 필요!

@Tag(name = "회원 관련 API")
@RestController
@RequestMapping("/api/member")
@Slf4j
public class MemberController {
  private final MemberService memberService;
  private final PlaylistService playlistService;
  private final S3Uploader s3Uploader;
  private final PasswordValidationService passwordValidationService;
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

  private final MemberChangePasswordRequestProvider memberChangePasswordRequestProvider;

  @Autowired
  public MemberController(MemberService memberService,
    PlaylistService playlistService,
    S3Uploader s3Uploader,
    PasswordValidationService passwordValidationService) {
    this.memberService = memberService;
    this.playlistService = playlistService;
    this.s3Uploader = s3Uploader;
    this.passwordValidationService = passwordValidationService;
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
  public ResponseEntity<?> signupNon(@Valid @RequestBody NonMemberRequestDto nonMemberRequestDto) {
    RequestUtil.checkNeedValue(
      nonMemberRequestDto.getDeviceId(),
      nonMemberRequestDto.getIsPC());

    Member nonMember = memberService.registerNon(nonMemberRequestDto.getDeviceId(),
      nonMemberRequestDto.getIsPC());
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
  public ResponseEntity<?> signupReal(@Valid @RequestBody MemberRequestDto memberRequestDto) {
    Member member = memberService.registerReal(memberRequestDto.getLoginId(),
      memberRequestDto.getPassword(), memberRequestDto.getIsPC());
    log.info("memberLoginId = {}", memberRequestDto.getLoginId());
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
  public ResponseEntity<?> login(@Valid @RequestBody MemberRequestDto memberRequestDto) {
    RequestUtil.checkNeedValue(
      memberRequestDto.getLoginId(),
      memberRequestDto.getIsPC());

    Member member = memberService.findMemberByLoginId(memberRequestDto.getLoginId());
    String password = member.isMember() ? memberRequestDto.getPassword() : member.getLoginId();
    Token token = memberService.login(memberRequestDto.getLoginId(), password,
      memberRequestDto.getIsPC());

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
  public ResponseEntity<?> reissue(@Valid @RequestBody TokenReissueRequestDto reissueRequestDto)
    throws Exception {
    RequestUtil.checkNeedValue(
      reissueRequestDto.getAccessToken(),
      reissueRequestDto.getRefreshToken(),
      reissueRequestDto.getIsPC());
    Token token = memberService.reissue(reissueRequestDto.getAccessToken(),
      reissueRequestDto.getRefreshToken(), reissueRequestDto.getIsPC());

    BaseResponseSuccessDto responseBody = new TokenResponseDto(token);
    return ResponseEntity.ok(responseBody);
  }





  // 일급 컬렉션
  private static Map<FailedReason, String> getMapper(MemberChangePasswordRequestDto dto) {
    Map<FailedReason, String> mapper = new HashMap<>();
    mapper.put(NOT_FOUND_MEMBER, "멤버가 없어요~ 이전에 생성했는지 보세요~ 잘못된 id: [%s]" + dto.getMemberId());
    mapper.put(NOT_MATCHED_OLD_PASSWORD, "비밀번호가 달라요~");
    mapper.put(UNEXPECTED_NEW_PASSWORD, "새로운 비밀번호가 규칙에 어긋나요~ 뭐 5글자 이상으로 해주세요~ (기타 조건 블라블라)");

    requires(mapper.size() == FailedReason.values().length);

    return mapper;
  }

  // 1. 여러군데에서 호출을 해도 까먹지 않고 cache까지 날려야 한다
  // 2. cache는 cache만, main은 main 로직만 처리해야 한다. SRP
  // 3. 테스트도 쉬워야 된다.


  @Autowired
  ChangingPassword changingPassword;

  @PostMapping("/changePassword")
  public ResponseEntity<?> changePassword(@RequestBody MemberChangePasswordRequestDto dto) {
    final Result result = memberChangePasswordRequestProvider.create(dto);


    if (result.isSuccess()) {
      changingPassword.changePassword(result.request());

      return ResponseEntity.ok().build();
    }


    return getResponseEntity(dto, result);
  }





  private ResponseEntity<?> getResponseEntity(MemberChangePasswordRequestDto dto, Result result) {
    final Set<FailedReason> reasons = result.failedReason();
    if (reasons.contains(NOT_MATCHED_OLD_PASSWORD)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    final String translated = FailedReasonTranslator.create(dto).translate(reasons);
    return ResponseEntity.badRequest().body(translated);
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
    @Valid @RequestBody MemberLoginRequestDto memberLoginRequestDto) {
    RequestUtil.checkNeedValue(memberLoginRequestDto.getLoginId(),
      memberLoginRequestDto.getPassword());
    log.info(" loginId = {}", authentication.getName());
    String loginId = authentication.getName();
    Member member = memberService.findMemberByLoginId(loginId);
    passwordValidationService.checkValidPassword(memberLoginRequestDto.getPassword());
    memberService.change(member, memberLoginRequestDto.getLoginId(),
      memberLoginRequestDto.getPassword());

    BaseResponseSuccessDto responseBody = new MemberResponseDto(member);
    return ResponseEntity.ok(responseBody);
  }
}
