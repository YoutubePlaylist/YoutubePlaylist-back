package com.example.youtubedb.controller;

import com.example.youtubedb.test.Member2;
import com.example.youtubedb.test.MemberLogin2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

  private final MemberLogin2 memberLogin2;

    @GetMapping("/admin/test")
    public String getTest() {
      Member2 login = memberLogin2.login("testId", "password");
      return login.loginId();
    }
}
