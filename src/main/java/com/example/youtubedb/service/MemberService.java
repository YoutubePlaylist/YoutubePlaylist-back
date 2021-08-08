package com.example.youtubedb.service;

import com.example.youtubedb.domain.Member;
import com.example.youtubedb.exception.DoNotMatchPasswordException;
import com.example.youtubedb.exception.DuplicateMemberException;
import com.example.youtubedb.exception.NotExistMemberException;
import com.example.youtubedb.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Member registerNon(String deviceId) {
        checkDuplicateMember(deviceId);
        Member nonMember = Member.builder()
                .isMember(false)
                .loginId(deviceId)
                .password(null)
                .build();

        return memberRepository.save(nonMember);
    }

    public Member register(String loginId, String password) { // TODO : 비밀번호 생성 규칙 필요!
        checkDuplicateMember(loginId);
        Member member = Member.builder()
                .isMember(true)
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .build();

        return memberRepository.save(member);
    }

    private void checkDuplicateMember(String loginId) {
        memberRepository.findByLoginId(loginId).ifPresent(m -> {
            throw new DuplicateMemberException();
        });
    }

    public Member login(String loginId, String password) {
        Member member = findMemberByLoginId(loginId);
        checkPassword(password, member.getPassword());

        return member;
    }

    private void checkPassword(String password, String encodedPassword) {
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new DoNotMatchPasswordException();
        }
    }

    public Member findMemberByLoginId(String loginId) {
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        member.orElseThrow(NotExistMemberException::new);
        return member.get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public void deleteUserByLoginId(String loginId) {
        Member member = findMemberByLoginId(loginId);
        memberRepository.delete(member);
    }
}
