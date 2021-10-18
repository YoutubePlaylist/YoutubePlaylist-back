package com.example.youtubedb.service;

import com.example.youtubedb.config.jwt.JwtResolver;
import com.example.youtubedb.config.jwt.TokenProvider;
import com.example.youtubedb.domain.member.Authority;
import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.domain.member.SecuredPassword;
import com.example.youtubedb.domain.token.Token;
import com.example.youtubedb.dto.member.MemberForTokenDto;
import com.example.youtubedb.exception.*;
import com.example.youtubedb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final MemberRepository memberRepository;
  private final TokenProvider tokenProvider;
  private final JwtResolver jwtResolver;
  private final RefreshTokenServiceImpl refreshTokenServiceImpl;
  private final SecuredPassword.Provider securedPasswordProvider;

  public Member registerNon(String deviceId, Boolean isPC) {
    checkDuplicateMember(deviceId);

    Member nonMember = Member.builder()
      .isMember(false)
      .loginId(deviceId)
      .authority(Authority.ROLE_USER)
      .password(securedPasswordProvider.create(deviceId))
      .isPC(isPC)
      .build();

    return memberRepository.save(nonMember);
  }

  public Member registerReal(String loginId, String password, Boolean isPC) {
    checkDuplicateMember(loginId);
    Member realMember = Member.builder()
      .isMember(true)
      .loginId(loginId)
      .authority(Authority.ROLE_USER)
      .password(securedPasswordProvider.create(password))
      .isPC(isPC)
      .build();

    return memberRepository.save(realMember);
  }

  public Member changePassword(Member updateMember, String oldPassword, String newPassword) {
    updateMember.setPassword(securedPasswordProvider.create(newPassword));
    refreshTokenServiceImpl.deleteRefreshToken(updateMember.getLoginId());

    return memberRepository.save(updateMember);
  }

  public Token login(MemberForTokenDto memberForTokenDto, String password) {
    try {
      Token token = tokenProvider.create(memberForTokenDto);

      refreshTokenServiceImpl.updateRefreshToken(memberForTokenDto, token.getRefreshToken());

      // 4. 토큰 발급
      return token;
    } catch (BadCredentialsException e) {
      throw new DoNotMatchPasswordException();
    }
  }


  @Transactional
  public Token reissue(String loginId, String refreshToken, boolean isPC) throws Exception {
    if (!jwtResolver.validateToken(refreshToken)) {
      throw new RefreshTokenException("Refresh Token 이 유효하지 않습니다.");
    }

    String redisRefreshToken = refreshTokenServiceImpl.getValueByKey(isPC, loginId);

    if (redisRefreshToken == null) {
      throw new RefreshTokenException("다시 로그인이 필요합니다.");
    }

    // 4. Refresh Token 일치하는지 검사
    if (!redisRefreshToken.equals(refreshToken)) {
      throw new RefreshTokenException("토큰의 유저 정보가 일치하지 않습니다.");
    }

    // 재발행 토큰 발급
    return tokenProvider.reissue(loginId, refreshToken);
  }

  public Member findMemberByLoginId(String loginId) {
    return memberRepository.findByLoginId(loginId).orElseThrow(NotExistMemberException::new);
  }

  private void checkDuplicateMember(String loginId) {
    memberRepository.findByLoginId(loginId).ifPresent(m -> {
      throw new DuplicateMemberException();
    });
  }

  private UsernamePasswordAuthenticationToken toAuthentication(String loginId, String password) {
    return new UsernamePasswordAuthenticationToken(loginId, password);
  }

  public void deleteUserByLoginId(String loginId) {
    Member member = findMemberByLoginId(loginId);
    memberRepository.delete(member);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    System.out.println("CustomUserDetailsService.loadUserByUsername");
    return memberRepository.findByLoginId(username)
      .map(this::createUserDetails)
      .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
  }

  private UserDetails createUserDetails(Member member) {
    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

    return new User(
      String.valueOf(member.getLoginId()),
      member.getPassword().getPassword(),
      Collections.singleton(grantedAuthority)
    );
  }

  public void setProfileImg(Member member, String profileImg) {
    member.setProfileImg(profileImg);
  }

  public void checkMember(Member member) {
    if (!member.isMember()) {
      throw new NotMemberException();
    }
  }

  public void change(Member member, String loginId, String password) {
    checkDuplicateMember(loginId);
    member.changeToMember(loginId, securedPasswordProvider.create(password));
  }
}
