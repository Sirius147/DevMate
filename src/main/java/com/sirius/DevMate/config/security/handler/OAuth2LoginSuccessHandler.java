package com.sirius.DevMate.config.security.handler;

import com.sirius.DevMate.config.security.jwt.service.JwtTokenService;
import com.sirius.DevMate.domain.common.sys.OAuth2Provider;
import com.sirius.DevMate.domain.user.User;
import com.sirius.DevMate.repository.user.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    @Value("${app.frontend.success-redirect}") private String frontendRedirect;
    @Value("${jwt.access-exp-seconds}") private Long accessExpSeconds;
    @Value("${jwt.refresh-exp-seconds}") private Long refreshExpSeconds;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException  {

        /* Authentication 으로부터
         access 토큰 -> provider 구하기,
          OAuth2User 정보 가져오기 -> providerId 구하기
           provider, providerId로 user 불러오기
        */

        // 기본 SavedRequest 리다이렉트를 무시하고 목적지를 직접 정함

        setAlwaysUseDefaultTargetUrl(true);
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String registrationId = token.getAuthorizedClientRegistrationId();
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attr = oAuth2User.getAttributes();
        OAuth2Provider provider = OAuth2Provider.valueOf(registrationId.toUpperCase());

        String providerId;
        if ("google".equals(registrationId)) {
            // sub는 문자열
            providerId = String.valueOf(attr.get("sub"));
        } else {
            // id는 Integer/Long일 수 있으므로 안전하게 string으로 변환
            providerId = String.valueOf(attr.get("id"));
        }

        Optional<User> opt = userRepository.findByProviderAndProviderId(provider, providerId);
        // 정말 드물지만, upsert 전에 핸들러가 먼저 불리면 방어적으로 에러
        if (opt.isEmpty()) {
            log.error("success logic before upsert, redirect to /auth/logout");
            getRedirectStrategy().sendRedirect(request, response, "auth/logout");
            return;
        }
        // User 불러오기
        User user = opt.get();

        /**
         * OAuth2로 로그인한
         * 실 사용자에게 발행할
         * jwt 토큰 pair
         */

        JwtTokenService.TokenPair tokenPair = jwtTokenService.issue(user.getUserId(), user.getRole());

        // 보안상 권장: refreshToken은 HttpOnly+Secure 쿠키로
        Cookie refreshTokenCookie = new Cookie("refresh_token", tokenPair.refresh());
        refreshTokenCookie.setHttpOnly(false);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setAttribute("same_site", "None");
        refreshTokenCookie.setPath("/");       // 모든 경로에 대하여 쿠키를 보냄
        refreshTokenCookie.setMaxAge(60*60*24*14);
        response.addCookie(refreshTokenCookie);

        Cookie acessTokenCookie = new Cookie("access_token", tokenPair.access());
        acessTokenCookie.setHttpOnly(false);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setAttribute("same_site", "None");
        acessTokenCookie.setPath("/");
        acessTokenCookie.setMaxAge(Math.toIntExact(accessExpSeconds));
        response.addCookie(acessTokenCookie);



//        refresh.setSecure(true);    // https 에서만 전송
//        refresh.setAttribute("SameSite", "Strict"); 다른 도메인에서 접근 가능

/*        // access token을 query paramters에 담아 전달
//         accessToken은 query parameter에 담아 프론트로 전달
//        String url = UriComponentsBuilder.fromUriString(frontendRedirect)
//                .queryParam("token", tokenPair.access())
//                .build().toUriString();
        // 최종 redirect to front
//        getRedirectStrategy().sendRedirect(request,response,url);
*/
        // access token을 response body 에 json으로 전달
//        Map<String, String> accessTokenJson = Map.of(
//                "token_type", "bearer_token",
//                "access_token", tokenPair.access()
//        );
//        new ObjectMapper().writeValue(response.getWriter(), accessTokenJson);

        getRedirectStrategy().sendRedirect(request,response,frontendRedirect);

//        // 최초 로그인 or 기존 로그인 ver Session

//        String dest = user.getCreatedAt().equals(user.getUpdatedAt()) ? "/login/basic" : "/main";
//        String url = UriComponentsBuilder.fromPath(dest) //fromUriString("localhost:8080" + dest)              //fromPath(dest)
//                .queryParam("access_token", tokenPair.access())    // access 토큰 query parameter로 전달
//                .build().toString();
//
//        getRedirectStrategy().sendRedirect(request, response, url);
//

    }
}
