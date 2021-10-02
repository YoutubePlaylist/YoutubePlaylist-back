package com.example.youtubedb.service.member;

import com.example.youtubedb.repository.interfaces.MemberRepository;
import com.example.youtubedb.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class MemberServiceConfig {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final PlaylistService playlistService;

  @Bean
  public NonMemberRegister nonMemberRegister() {
    NonMemberRegister memberDBUpdate = new NonMemberRegister.UpdateDatabase(memberRepository, passwordEncoder);
    NonMemberRegister addMemberPlaylist = new NonMemberRegister.CreatePlaylist(memberDBUpdate, playlistService);

    return addMemberPlaylist;
  }
}
