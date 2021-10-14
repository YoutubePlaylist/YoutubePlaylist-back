package com.example.youtubedb.test;

interface MyEncoder {
  String encode(String rawPassword);
  boolean matches(String rawPassword, String encodedPassword);
}
