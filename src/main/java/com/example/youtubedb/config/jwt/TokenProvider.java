package com.example.youtubedb.config.jwt;

import com.example.youtubedb.domain.token.AccessToken;
import com.example.youtubedb.domain.token.JwtToken;
import com.example.youtubedb.domain.token.RefreshToken;
import com.example.youtubedb.domain.token.Token;
import com.example.youtubedb.exception.NotExistAuthorityException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static java.sql.Timestamp.valueOf;

@Slf4j
@Component
public class TokenProvider {

	private final String AUTHORITIES_KEY = "auth";

	private final Key key;

	@Autowired
	public TokenProvider(@Value("${jwt.secret}") String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public Token generateTokenDto(Authentication authentication, boolean isPC) {
		// Access Token 생성
		AccessToken accessToken = new AccessToken(authentication.getName(), DateUtil.getAccessTokenExpiresIn().toInstant());

		// Refresh Token 생성
		RefreshToken refreshToken = new RefreshToken(DateUtil.getRefreshTokenExpiresIn(isPC).toInstant());

		return JwtToken.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	public Authentication getAuthentication(String accessToken) {
		// 토큰 복호화
		Claims claims = parseClaims(accessToken);


		if (claims.get(AUTHORITIES_KEY) == null) {
			throw new NotExistAuthorityException();
		}
		// 클레임에서 권한 정보 가져오기
		Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());

		// UserDetails 객체를 만들어서 Authentication 리턴
		UserDetails principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

	public boolean validateToken(String token) throws Exception {
		Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
		return true;
	}

	//    public String parse
	private Claims parseClaims(String accessToken) throws ExpiredJwtException {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
	}

	private static class DateUtil {
		private static final Duration ACCESS_TOKEN_EXPIRE_TIME = Duration.ofMinutes(30);
		private static final Period REFRESH_TOKEN_EXPIRE_DATE_APP = Period.ofDays(7);
		private static final Period REFRESH_TOKEN_EXPIRE_DATE_PC = Period.ofMonths(3);

		private static Date getAccessTokenExpiresIn() {
			return valueOf(LocalDateTime.now().plus(ACCESS_TOKEN_EXPIRE_TIME));
		}

		private static Date getRefreshTokenExpiresIn(boolean isPC) {
			return valueOf(LocalDateTime.now().plus(isPC ? REFRESH_TOKEN_EXPIRE_DATE_PC : REFRESH_TOKEN_EXPIRE_DATE_APP));
		}
	}
}