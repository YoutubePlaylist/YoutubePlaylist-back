package com.example.youtubedb.test1;

class MemberLogin3 {

  //todo : client 입장에서 생각 했을 때 기존 방법은 repository 를 주입해주고, encoder도 주입해주고 해야할게 많다.
  // method 만 보고 사용하기 위해서는 client 가 의존성 주입해주지 않고도 사용 할 수 있어야 하지 않을까?
  private final CheckPassword checkPassword = new CheckPassword();

  boolean login(String savedPassword, String inputPassword) {
    return checkPassword.check(savedPassword, inputPassword);
  }
}
