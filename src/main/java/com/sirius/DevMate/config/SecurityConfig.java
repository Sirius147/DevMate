package com.sirius.DevMate.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * - 인가 규칙(permitAll / authenticated)
     * - OAuth2 로그인(로그인 페이지, 사용자 정보 로딩, 성공/실패 핸들러)
     * - 로그아웃 동작
     * - (필요 시) H2 콘솔 사용을 위한 예외 설정

     *  파라미터로 받는 Bean
     *   - customOAuth2UserService: 소셜 프로필을 우리 도메인 유저로 매핑(Upsert)하는 커스텀 서비스
     *   - successHandler: 로그인 성공 후 리다이렉트/분기(프로필 작성 유도 등) 처리
     *   둘 중 없는 것이 있다면, 해당 파라미터와 설정 라인을 제거해도 됩니다.
     */
    @Bean
    SecurityFilterChain filterChain(
            HttpSecurity http,
            OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService, // 없으면 파라미터 제거
            AuthenticationSuccessHandler successHandler // 없으면 파라미터 & .successHandler(...) 제거
    ) throws Exception {

        // 1) URL별 인가(접근권한) 규칙
        http.authorizeHttpRequests(auth -> auth
                // 정적 리소스나 공개 페이지는 누구나 접근 가능
                .requestMatchers(
                        "/", "/login", "/error",
                        "/css/**", "/js/**", "/images/**", "/favicon.ico"
                ).permitAll()
                // 그 외 모든 요청은 인증 필수
                .anyRequest().authenticated()
        );

        // 2) OAuth2 로그인 설정
        http.oauth2Login(oauth -> oauth
                        // (선택) 커스텀 로그인 페이지 경로. 기본 페이지를 쓸 거면 이 라인 제거
                        .loginPage("/login")

                        // (중요) 사용자 정보 로드 시 커스텀 OAuth2UserService 사용
                        //   - 여기서 제공자(Google/GitHub) 프로필을 받아
                        //     우리 DB(User 엔티티)에 Upsert 하고 권한(ROLE_USER) 부여 등 처리
                        .userInfoEndpoint(user -> user.userService(customOAuth2UserService))

                        // (선택) 로그인 성공 후 분기 처리 (최초 로그인 → 프로필 작성 등)
                        //   - successHandler가 없다면 이 라인을 제거하고
                        //     대신 .defaultSuccessUrl("/", true) 등을 사용할 수 있음
                        .successHandler(successHandler)

                // (참고) 실패 핸들러 커스터마이징도 가능 (필요 시)
                // .failureHandler(customFailureHandler)
        );

        // 3) 로그아웃 설정
        http.logout(logout -> logout
                        // 기본 로그아웃 URL은 POST /logout
                        // 프론트가 GET을 원하면 추가 설정 필요 (안전성을 위해 기본 권장)
                        .logoutSuccessUrl("/") // 로그아웃 후 이동할 페이지
                // .invalidateHttpSession(true) // 세션 무효화(기본 true)
        );


        // 최종 SecurityFilterChain 빌드
        return http.build();
    }

    /*
     * (선택) PasswordEncoder
     * - 소셜 로그인만 쓰더라도, 추후 내부 계정/비밀번호 기반 로그인을 병행할 가능성이 있으면 추가해두면 유용.
     *
     * @Bean
     * public PasswordEncoder passwordEncoder() {
     *     return new BCryptPasswordEncoder();
     * }
     */

    /*
     * 기본 동작 요약
     * - 인증 필요 자원 접근 시, Spring Security가 자동으로 /oauth2/authorization/{registrationId}로 유도
     *   예) /oauth2/authorization/google, /oauth2/authorization/github
     * - 인증 성공 후:
     *   - successHandler가 있으면 해당 로직으로 분기
     *   - 없으면 defaultSuccessUrl 또는 요청했던 원래 URL로 리다이렉트
     * - @EnableMethodSecurity 덕분에 @PreAuthorize("hasRole('ADMIN')") 같은 메소드 보안도 사용 가능
     */
}
