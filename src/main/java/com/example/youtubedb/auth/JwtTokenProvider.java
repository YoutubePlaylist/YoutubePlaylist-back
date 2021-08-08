package com.example.youtubedb.auth;

import com.example.youtubedb.domain.Member;
import com.example.youtubedb.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final MemberService memberService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.time}")
    private Long tokenValidTime;

    @PostConstruct
    protected void init() {
        System.out.println("SecretKey = " + secretKey);
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        System.out.println("Post secretKey = " + secretKey);
    }

    public String createToken(String userPk, String role, Boolean isPC) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", role);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + checkPC(isPC)))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private long checkPC(Boolean isPC) {
        long tokenTime = tokenValidTime;
        if (isPC) {
            tokenTime /= 2;
        }
        System.out.println("tokenValidTime = " + tokenValidTime);
        System.out.println("realTokenTime = " + tokenTime);
        return tokenTime;
    }

    public Authentication getAuthentication(String token) {
        Member member = memberService.findMemberByLoginId(this.getUserPk(token));

        return new UsernamePasswordAuthenticationToken(member, "", member.getAuthorities());
    }

    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public void abandonToken(String token) {
        Date now = new Date();
        System.out.println(now);
        System.out.println(validateToken(token));
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
