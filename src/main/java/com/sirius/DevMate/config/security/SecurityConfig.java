package com.sirius.DevMate.config.security;

import com.sirius.DevMate.config.security.handler.OAuth2LoginFailureHandler;
import com.sirius.DevMate.config.security.handler.OAuth2LoginSuccessHandler;
import com.sirius.DevMate.config.security.oauth2.service.GithubOAuth2UserService;
import com.sirius.DevMate.config.security.oauth2.service.GoogleOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
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
                        "/","/login", "/error",
                        "/css/**", "/js/**", "/images/**", "/favicon.ico", "/oauth2/**", "/auth/refresh"
                ).permitAll()
                .anyRequest().authenticated()
                )
                .cors(c -> {})
                .csrf(csrf -> csrf.disable())   // 개발 시에 csrf 보호 비활성화
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .sessionManagement(sm ->
//                        sm.sessionFixation(sf -> sf.none()))
//                .sessionManagement(sm -> sm
//                        .sessionFixation(sf -> sf.migrateSession())
////                        .maximumSessions(2) // 포스트맨 테스트를 위해
//                )
                .oauth2Login(oauth2 -> oauth2
                                // userService 호출
                                //  커스텀 로그인 페이지가 있다면 지정  .loginPage("/login")
                                .userInfoEndpoint(u -> u.userService(githubOAuth2UserService)
                                        .oidcUserService(googleOidcUserService)
                                )
                                .successHandler(oAuth2LoginSuccessHandler)
                                .failureHandler(oAuth2LoginFailureHandler)
                )
                .oauth2ResourceServer(rs -> rs
                        .jwt(j -> j
                                .decoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                        .authenticationEntryPoint(
                                (req, res, ex) -> res.sendError(401, "Unauthorized"))
                );

        // 최종 SecurityFilterChain 빌드
        return http.build();
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
