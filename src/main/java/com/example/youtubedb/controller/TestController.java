package com.example.youtubedb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/admin/test")
    public String getTest() {
        return "ADMIN TEST";
    }
}
