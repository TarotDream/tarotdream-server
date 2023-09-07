package com.sunkyuj.tarotdream.config;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // SecurityFilterChain에서 요청에 접근할 수 있어서 인증, 인가 서비스에 사용

        //basic
        httpSecurity
                .httpBasic().disable()      // http basic auth 기반으로 로그인 인증창이 뜬다. 기본 인증을 이용하지 않으려면 .disable()을 추가해준다.
                .csrf().disable()       // csrf, api server이용시 .disable (html tag를 통한 공격)
                .cors();	 //  다른 도메인의 리소스에 대해 접근이 허용되는지 체크
        // session
        httpSecurity
                .sessionManagement()        // 세션 관리 기능을 작동한다.      .maximunSessions(숫자)로 최대 허용가능 세션 수를 정할수 있다.(-1로 하면 무제한 허용)
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // jwt사용하는 경우 씀(STATELESS는 인증 정보를 서버에 담지 않는다.)
        // authorizeHttpRequests
        httpSecurity
                .authorizeHttpRequests()
                .anyRequest().permitAll();

        return httpSecurity.build();
    }
}

