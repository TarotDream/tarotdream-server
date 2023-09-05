package com.sunkyuj.tarotdream.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProvider jwtProvider;

    @Value("${jwt.jwt-secret-key}")
    private String JWT_SECRET_KEY;
    @Value("${jwt.token-header-prefix}")
    private String TOKEN_HEADER_PREFIX;

    final String INVALID_JWT = "유효하지 않은 토큰입니다.";
    final String EMPTY_JWT = "토큰이 비어있습니다.";

    public LoginToken createToken(Long id) {
        String accessToken = jwtProvider.publishAccessToken(id);
        String refreshToken = jwtProvider.publishRefreshToken(id);
        return LoginToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expireSec(jwtProvider.getAccessTokenExpireTime())
                .build();
    }

    public LoginToken createTokenByRefreshToken(String refreshToken) throws Exception {
        Long id = jwtProvider.getIdFromToken(refreshToken);
        String accessToken = jwtProvider.publishAccessToken(id);
        String newRefreshToken = jwtProvider.publishRefreshToken(id);
        return LoginToken.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .expireSec(jwtProvider.getAccessTokenExpireTime())
                .build();
    }

    //헤더에서 JWT 추출
    public String getJwt() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization");
    }

    // JWT에서 값 추출
    public Long getUserId() throws Exception {
        // 헤더에서 JWT 추출
        String accessToken = getJwt();
        accessToken = accessToken.replace(TOKEN_HEADER_PREFIX,"");
        if (accessToken == null || accessToken.length() == 0) {
            throw new Exception(EMPTY_JWT);
        }

        // JWT 파싱
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(JWT_SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

        } catch (Exception ignored) {
            throw new Exception(INVALID_JWT);
        }

        // id 추출
        return claims.get("userId", Long.class);
    }

    /*
    JWT에서 exp 추출
        @return int
    @throws BaseException
     */
    public Date getExp() throws Exception{
        //1. JWT 추출
        String accessToken = getJwt();
        accessToken = accessToken.replace(TOKEN_HEADER_PREFIX,"");
        System.out.println("accessToken = " + accessToken);
        if(accessToken == null || accessToken.length() == 0){
            throw new Exception(EMPTY_JWT);
        }

        // 2. JWT parsing
        Claims claims;
        try{
            claims = Jwts.parserBuilder()
                    .setSigningKey(JWT_SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (Exception ignored) {
            throw new Exception(INVALID_JWT);
        }

        // 3. exp
        return claims.get("exp",Date.class);
    }

}
