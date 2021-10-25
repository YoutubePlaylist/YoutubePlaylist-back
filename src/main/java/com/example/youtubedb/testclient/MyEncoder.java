package com.example.youtubedb.testclient;

class MyEncoder {
  String encode(String rawPassword){
    return rawPassword;
  };

  boolean matches(String rawPassword, String encodedPassword){
    return rawPassword.equals(encodedPassword);
  };
}
