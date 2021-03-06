package com.example.youtubedb.service;

import com.example.youtubedb.config.jwt.TokenProvider;
import com.example.youtubedb.domain.Token;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        checkValidPassword(password);
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
        checkCorrectPassword(updateMember, oldPassword, newPassword);
        checkValidPassword(newPassword);

        updateMember.setPassword(passwordEncoder.encode(newPassword));
        template.delete("PC" + updateMember.getLoginId());
        template.delete("APP" + updateMember.getLoginId());

        return memberRepository.save(updateMember);
    }

    private void checkCorrectPassword(Member updateMember, String oldPassword, String newPassword) {
        if(!passwordEncoder.matches(oldPassword, updateMember.getPassword())){
            throw new DoNotMatchPasswordException();
        }
        if (oldPassword.equals(newPassword)) {
            throw new DoNotChangePasswordException();
        }
    }


    private void checkValidPassword(String password) {
        int min = 8;
        int max = 20;
        // ??????, ??????, ???????????? ?????? min~max??????
        final String regex = "^((?=.*\\d)(?=.*[a-zA-Z])(?=.*[\\W]).{" + min + "," + max + "})$";
        // ?????? ?????? ?????????
        final String blankRegex = "(\\s)";

        Matcher matcher;

        // ?????? ??????
        matcher = Pattern.compile(blankRegex).matcher(password);
        if (matcher.find()) {
            throw new InvalidBlankPasswordException();
        }

        // ????????? ??????
        matcher = Pattern.compile(regex).matcher(password);
        if (!matcher.find()) {
            throw new InvalidRegexPasswordException();
        }
    }

    public Token login(String loginID, String password, boolean isPC) {
        // 1. Login ID/PW ??? ???????????? AuthenticationToken ??????
        UsernamePasswordAuthenticationToken authenticationToken = toAuthentication(loginID, password);

        // 2. ????????? ?????? (????????? ???????????? ??????) ??? ??????????????? ??????
        //    authenticate ???????????? ????????? ??? ??? CustomUserDetailsService ?????? ???????????? loadUserByUsername ???????????? ?????????
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            System.out.println("authentication = " + authentication.getName());
            // 3. ?????? ????????? ???????????? JWT ?????? ??????
            Token token = tokenProvider.generateTokenDto(authentication, isPC);



            long now = (new Date()).getTime();
            if(isPC) {
            stringStringValueOperations.set("PC"+authentication.getName(), token.getRefreshToken(), token.getRefreshTokenExpiresIn().getTime()- now, TimeUnit.MILLISECONDS);
            }else {
            stringStringValueOperations.set("APP"+authentication.getName(), token.getRefreshToken(), token.getRefreshTokenExpiresIn().getTime() -now, TimeUnit.MILLISECONDS);
            }

            // 4. ?????? ??????
            return token;
        } catch (BadCredentialsException e) {
            throw new DoNotMatchPasswordException();
        }
    }


    @Transactional
    public Token reissue(String accessToken, String refreshToken, boolean isPC) {
        // 1. Refresh Token ??????
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RefreshTokenException("Refresh Token ??? ???????????? ????????????.");
        }

        // 2. Access Token ?????? Member ID(pk) ????????????
        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        //3. Redis?????? ?????? ?????????
        String redisRefreshToken;
        if(isPC) {
           redisRefreshToken = stringStringValueOperations.get("PC"+authentication.getName());
        }else {
           redisRefreshToken = stringStringValueOperations.get("APP"+authentication.getName());
        }

        if(redisRefreshToken == null) {
            throw new RefreshTokenException("?????? ???????????? ???????????????.");
        }

        // 4. Refresh Token ??????????????? ??????
        if (!redisRefreshToken.equals(refreshToken)) {
            throw new RefreshTokenException("????????? ?????? ????????? ???????????? ????????????.");
        }

        // 5. ????????? ?????? ??????
        Token tokenDto = tokenProvider.generateTokenDto(authentication, isPC);
        tokenDto.setRefreshToken(redisRefreshToken);

        // ?????? ??????
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
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> ???????????????????????? ?????? ??? ????????????."));
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
        checkValidPassword(password);
        member.changeToMember(loginId, passwordEncoder.encode(password));
    }
}
