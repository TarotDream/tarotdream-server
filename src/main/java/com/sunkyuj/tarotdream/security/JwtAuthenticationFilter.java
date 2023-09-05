package com.sunkyuj.tarotdream.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Value("${jwt.token-header-prefix}")
    private String TOKEN_HEADER_PREFIX;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException, ServletException, IOException {

        String authHeader = request.getHeader(AUTHORIZATION);

        if (StringUtils.hasText(authHeader)) {
            String accessToken = authHeader.replace(TOKEN_HEADER_PREFIX, "");
            Long userId = null;
            try {
                userId = jwtProvider.getIdFromToken(accessToken);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (userId == null) {
                throw new InvalidParameterException("유효하지 않은 토큰입니다.");
            }
            // key id로 userId, password 불러와서 로그인
            Authentication authentication = new UsernamePasswordAuthenticationToken(userId, "password", getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_USER"));

        return grantedAuthorityList;
    }
}