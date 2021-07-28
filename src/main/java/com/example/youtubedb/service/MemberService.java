package com.example.youtubedb.service;

import com.example.youtubedb.domain.Member;
import com.example.youtubedb.exception.DuplicateMemberException;
import com.example.youtubedb.exception.NotExistMemberException;
import com.example.youtubedb.repository.MemberRepository;
import com.example.youtubedb.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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

    private void checkDuplicateMember(String deviceId) {
        memberRepository.findByLoginId(deviceId).ifPresent(m -> { throw new DuplicateMemberException(); });
    }

    public Member findMemberByLoginId(String loginId) {
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        member.orElseThrow(NotExistMemberException::new);
        return member.get();
    }
}
