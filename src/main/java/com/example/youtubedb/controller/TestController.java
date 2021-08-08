package com.example.youtubedb.controller;

import com.example.youtubedb.domain.Member;
import com.example.youtubedb.dto.BaseResponseSuccessDto;
import com.example.youtubedb.dto.member.request.NonMemberRequestDto;
import com.example.youtubedb.dto.member.response.NonMemberResponseDto;
import com.example.youtubedb.util.RequestUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/admin/test")
    public String getTest() {
        return "ADMIN TEST";
    }
}
