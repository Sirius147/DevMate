package com.sirius.DevMate.config.security;

import com.sirius.DevMate.config.security.handler.OAuth2LoginFailureHandler;
import com.sirius.DevMate.config.security.handler.OAuth2LoginSuccessHandler;
import com.sirius.DevMate.config.security.oauth2.service.GithubOAuth2UserService;
import com.sirius.DevMate.config.security.oauth2.service.GoogleOidcUserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
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
            , JwtDecoder jwtDecoder
//            , ClientRegistrationRepository repo // google 인증 서버 로드 리졸버 용
    ) throws Exception {

        // 1) URL별 인가(접근권한) 규칙
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/error"//"/login",
                                , "/css/**", "/js/**", "/images/**", "/favicon.ico"
                                , "/oauth2/**"
                                , "/auth/**"
                                ,"/.well-known/**"
                                , "/ws-chat/**"     // stomp
                                , "/actuator/health", "/actuator/info"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .cors(c -> {
                })
                .csrf(csrf -> csrf.disable())   // 개발 시에 csrf 보호 비활성화
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 전역 exception handling을 bearer entrypoint로
                .exceptionHandling(ex -> ex.authenticationEntryPoint(bearerAuthEntryPoint()))
//                .sessionManagement(sm ->
//                        sm.sessionFixation(sf -> sf.none()))
//                .sessionManagement(sm -> sm
//                        .sessionFixation(sf -> sf.migrateSession())
////                        .maximumSessions(2) // 포스트맨 테스트를 위해
//                )
                .oauth2Login(oauth2 -> oauth2
                        // userService 호출
                        //  커스텀 로그인 페이지가 있다면 지정  .loginPage("/login")
                        .userInfoEndpoint(
                                u -> u.userService(githubOAuth2UserService)
                                        .oidcUserService(googleOidcUserService)
                        )
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                )
                /* 이 애플리케이션을 리소스 서버 모드로 켜고,
                요청에서 Authorization: Bearer <JWT>를 읽어 인증하는
                 BearerTokenAuthenticationFilter 등을 등록합니다.
                    즉, 이후 모든 보호된 API에서 JWT로 인증을 시도하게 됩니다.
                * */
                .oauth2ResourceServer(
                        rs -> rs.jwt(
                                        j -> j
                                                .decoder(jwtDecoder)    // 실제 토큰 검증 Decoder 등록
                                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                                )
                                .authenticationEntryPoint(
                                        bearerAuthEntryPoint())
                )
        ;

        // 최종 SecurityFilterChain 빌드
        return http.build();
    }

    // (1) 공통 EntryPoint: 만료/무효 토큰에 401 JSON + 표준 헤더
    @Bean
    AuthenticationEntryPoint bearerAuthEntryPoint() {
        return (req, res, ex) -> {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json;charset=UTF-8");
            // 표준 헤더로 클라이언트가 토큰 오류임을 인지하기 쉽게
            res.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer error=\"invalid_token\"");
            res.getWriter().write("""
                {"error":"ACCESS_TOKEN_INVALID_OR_EXPIRED",
                 "message":"Access token is missing, invalid, or expired."}
                """);
        };
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        var conv = new JwtGrantedAuthoritiesConverter();
        conv.setAuthoritiesClaimName("roles");
        conv.setAuthorityPrefix("ROLE_");
        var jwtConv = new JwtAuthenticationConverter();
        jwtConv.setJwtGrantedAuthoritiesConverter(conv);
        return jwtConv;
    }

}
