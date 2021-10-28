package com.example.youtubedb.member.login.spring;

import com.example.youtubedb.member.login.core.LoginRequest;

interface LoginRequestParser<T> {
  LoginRequest parse(T t);
}
