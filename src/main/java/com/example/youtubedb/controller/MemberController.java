package com.example.youtubedb.controller;

import com.example.youtubedb.domain.Member;
import com.example.youtubedb.dto.ResponseDto;
import com.example.youtubedb.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register/non")
    public ResponseEntity<?> registerNonMember(@RequestBody Map<String, String> request) {
        String deviceId = request.get("deviceId");
        Member nonMember = memberService.registerNon(deviceId);

        ResponseDto responseBody = ResponseDto.builder()
                .success(true)
                .response(nonMember)
                .error(null)
                .build();

        return ResponseEntity.ok(responseBody);
    }
}
