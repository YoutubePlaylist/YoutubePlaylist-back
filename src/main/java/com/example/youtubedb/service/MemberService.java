package com.example.youtubedb.service;

import com.example.youtubedb.config.jwt.TokenProvider;
import com.example.youtubedb.domain.token.Token;
import com.example.youtubedb.domain.member.Authority;
import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.exception.*;
import com.example.youtubedb.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService implements UserDetailsService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final StringRedisTemplate template;
    private final ValueOperations<String, String> stringStringValueOperations;

    @Autowired
    public MemberService(AuthenticationManagerBuilder authenticationManagerBuilder,
                         MemberRepository memberRepository,
                         PasswordEncoder passwordEncoder,
                         TokenProvider tokenProvider,
                         StringRedisTemplate template) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.template = template;
        this.stringStringValueOperations = template.opsForValue();
    }

    public Member registerNon(String deviceId, Boolean isPC) {
        checkDuplicateMember(deviceId);

        Member nonMember = Member.builder()
                .isMember(false)
                .loginId(deviceId)
                .authority(Authority.ROLE_USER)
                .password(passwordEncoder.encode(deviceId))
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
                .password(passwordEncoder.encode(password))
                .isPC(isPC)
                .build();

        return memberRepository.save(realMember);
    }

    public Member changePassword(Member updateMember, String oldPassword, String newPassword) {
        updateMember.setPassword(passwordEncoder.encode(newPassword));
        template.delete("PC" + updateMember.getLoginId());
        template.delete("APP" + updateMember.getLoginId());

        return memberRepository.save(updateMember);
    }

    public Token login(String loginID, String password, boolean isPC) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = toAuthentication(loginID, password);

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            System.out.println("authentication = " + authentication.getName());
            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            Token token = tokenProvider.generateTokenDto(authentication, isPC);

            long now = (new Date()).getTime();
            if(isPC) {
            stringStringValueOperations.set("PC"+authentication.getName(), token.getRefreshToken(), token.getRefreshTokenExpiresIn().getTime() - now, TimeUnit.MILLISECONDS);
            }else {
            stringStringValueOperations.set("APP"+authentication.getName(), token.getRefreshToken(), token.getRefreshTokenExpiresIn().getTime() - now, TimeUnit.MILLISECONDS);
            }

            // 4. 토큰 발급
            return token;
        } catch (BadCredentialsException e) {
            throw new DoNotMatchPasswordException();
        }
    }


    @Transactional
    public Token reissue(String accessToken, String refreshToken, boolean isPC) throws Exception {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RefreshTokenException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID(pk) 가져오기
        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        //3. Redis에서 파일 불러옴
        String redisRefreshToken;
        if(isPC) {
           redisRefreshToken = stringStringValueOperations.get("PC"+authentication.getName());
        }else {
           redisRefreshToken = stringStringValueOperations.get("APP"+authentication.getName());
        }

        if(redisRefreshToken == null) {
            throw new RefreshTokenException("다시 로그인이 필요합니다.");
        }

        // 4. Refresh Token 일치하는지 검사
        if (!redisRefreshToken.equals(refreshToken)) {
            throw new RefreshTokenException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        Token tokenDto = tokenProvider.generateTokenDto(authentication, isPC);
        tokenDto.setRefreshToken(redisRefreshToken);

        // 토큰 발급
        return tokenDto;
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
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }

    public void setProfileImg(Member member, String profileImg) {
        member.setProfileImg(profileImg);
    }

    public void checkMember(Member member) {
        if(!member.isMember()) {
            throw new NotMemberException();
        }
    }

    public void change(Member member, String loginId, String password) {
        checkDuplicateMember(loginId);
        member.changeToMember(loginId, passwordEncoder.encode(password));
    }
}
