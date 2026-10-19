package com.green.spring_board.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component

// 토큰 생성 및 검증, 정보 추출 및 만료처리 기능을 모아둔 클래스

public class JwtTokenProvider {
 private  final Key key;
 //액세스 토큰의 유효기간(밀리초)
 private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60;

 // 리프레시 토큰의 유효기간(밀리초)
    private static final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24 * 7;

 public JwtTokenProvider(
         @Value("${spring.jwt.secret}") String secretKey
 ){// this 키초기화
     this.key= Keys.hmacShaKeyFor(secretKey.getBytes());
 }
// 액세스 토큰 생성
    public String generateAccessToken(String  username){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY);

        return Jwts.builder()
                .setSubject(username)
                .setId(UUID.randomUUID().toString())
                .claim("tokenType", "ACCESS")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }
    // 리프레시 토큰 생성
    public String generateRefreshAccessToken(String  username){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + REFRESH_TOKEN_VALIDITY);

        return Jwts.builder()
                .setSubject(username)
                .setId(UUID.randomUUID().toString())
                .claim("tokenType", "REFRESH")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    // 액세스 토큰 유효성 (위/ 변조 여부)검증
    public boolean validateAccessToken(String  token){
        try {
            String tokenType = Jwts.parserBuilder()

                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("tokenType",String.class);
            return tokenType.equals("ACCESS");
        }catch (JwtException | IllegalArgumentException e){
            return false;
        }

    }
// 리프레시 액세스 토큰 유효성 (위/ 변조 여부)검증
    public boolean validateRefreshToken(String  token){
        try {
            String tokenType = Jwts.parserBuilder()

                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("tokenType",String.class);
            return tokenType.equals("REFRESH");
        }catch (JwtException | IllegalArgumentException e){
            return false;
        }

    }
    public String getUsernameFromToken(String token){
     return  Jwts.parserBuilder()
             .setSigningKey(key)
             .build()
             .parseClaimsJws(token)
             .getBody()
             .getSubject();

    }
    // JWT 토큰에서 만료일자 추출
    public Date getExpiration(String token){
        return  Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

    }





}
