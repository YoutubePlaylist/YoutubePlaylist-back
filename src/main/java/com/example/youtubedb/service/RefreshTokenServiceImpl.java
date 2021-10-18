package com.example.youtubedb.service;

import com.example.youtubedb.config.jwt.JwtFormatter;
import com.example.youtubedb.domain.token.RefreshToken;
import com.example.youtubedb.domain.token.Type;
import com.example.youtubedb.dto.member.MemberForTokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenServiceImpl extends RefreshTokenService<String, String> {
  private final JwtFormatter formatter;

  @Autowired
  public RefreshTokenServiceImpl(JwtFormatter formatter, RefreshTokenObserver refreshTokenObserver) {
    this.formatter = formatter;
    this.attach(refreshTokenObserver);
  }

  public void updateRefreshToken(MemberForTokenDto memberForTokenDto, RefreshToken refreshToken) {
    String key = getTypeName(memberForTokenDto.isPc()) + memberForTokenDto.getLoginId();
    String value = formatter.toJwtFromRefreshToken(refreshToken);
    notifyObserversForUpdate(key, value);
  }

  public void deleteRefreshToken(String loginId) {
    String APPKey = Type.APP.name() + loginId;
    String PCKey = Type.PC.name() + loginId;
    notifyObserversForDelete(APPKey);
    notifyObserversForDelete(PCKey);
  }

  public String getValueByKey(boolean isPC, String loginId) {
    String key = getTypeName(isPC) + loginId;
    return (String) observer.getValue(key);
  }

  private String getTypeName(boolean isPC) {
    if (isPC) {
      return Type.PC.name();
    }
    return Type.APP.name();
  }
}
