package com.sirius.DevMate.config.security;

import com.sirius.DevMate.config.security.handler.OAuth2LoginFailureHandler;
import com.sirius.DevMate.config.security.handler.OAuth2LoginSuccessHandler;
import com.sirius.DevMate.config.security.service.GithubOAuth2UserService;
import com.sirius.DevMate.config.security.service.GoogleOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final GithubOAuth2UserService githubOAuth2UserService;
    private final GoogleOidcUserService googleOidcUserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http
//            , ClientRegistrationRepository repo // google 인증 서버 로드 리졸버 용
    ) throws Exception {

        // 1) URL별 인가(접근권한) 규칙
        http.authorizeHttpRequests(auth -> auth
                // 정적 리소스나 공개 페이지는 누구나 접근 가능
                .requestMatchers(
                        "/","/login", "/error",
                        "/css/**", "/js/**", "/images/**", "/favicon.ico", "/oauth2/**"
                ).permitAll()
                // 그 외 모든 요청은 인증 필수
                .anyRequest().authenticated()
                ).
                oauth2Login(oauth2 -> oauth2
                        // userService 호출
                        //  커스텀 로그인 페이지가 있다면 지정
//                        .loginPage("/login")
                        .userInfoEndpoint(u -> u.userService(githubOAuth2UserService)
                                                .oidcUserService(googleOidcUserService)
                        ) //customOAuth2UserService
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
//                        .defaultSuccessUrl("/login",true) // 성공 시 항상 /login으로 이동
//                        .failureUrl("/login?error")
                ).logout(logout -> logout
                        .logoutUrl("/logout").logoutSuccessUrl("/")
                        .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                )
                .sessionManagement(sm ->
                        sm.sessionFixation(sf -> sf.none()))
//                .sessionManagement(sm -> sm
//                        .sessionFixation(sf -> sf.migrateSession())
////                        .maximumSessions(2) // 포스트맨 테스트를 위해
//                )
                .csrf(csrf -> csrf.disable());  // 개발 시에 csrf 보호 비활성화


        // 최종 SecurityFilterChain 빌드
        return http.build();
    }


}
