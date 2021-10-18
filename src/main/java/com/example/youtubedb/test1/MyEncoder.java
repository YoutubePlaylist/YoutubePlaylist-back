package com.example.youtubedb.test1;

class MyEncoder {
  String encode(String rawPassword){
    return rawPassword;
  };

  boolean matches(String rawPassword, String encodedPassword){
    return rawPassword.equals(encodedPassword);
  };
}
