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
import org.springframework.web.util.UriComponentsBuilder;

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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException  {

        // 기본 SavedRequest 리다이렉트를 무시하고 목적지를 직접 정함
        setAlwaysUseDefaultTargetUrl(true);
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String registrationId = token.getAuthorizedClientRegistrationId(); // "google" | "github"
        // OAuth2User 로부터
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attr = oAuth2User.getAttributes();
        // -> providerId
        OAuth2Provider provider = OAuth2Provider.valueOf(registrationId.toUpperCase());
        String providerId;
        // 를 구해서 User load
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
            log.error("upsert 전에 successHandler가 먼저 호출!");
            getRedirectStrategy().sendRedirect(request, response, "/logout?error=callSuccessHandlerBeforeUpsert");
            return;
        }

        // User 불러오기
        User user = opt.get();

        /**
         * 실 사용자에게
         * jwt 토큰 발급
         */

        JwtTokenService.TokenPair tokenPair = jwtTokenService.issue(user.getUserId(), user.getRole());

        /**
         *
         * refresh token은 https + cookie로 브라우저에 전달
         * access token은 query parameter로 전달
         */

        // 보안상 권장: refreshToken은 HttpOnly+Secure 쿠키로
        Cookie refresh = new Cookie("refresh_token", tokenPair.refresh());
        refresh.setHttpOnly(true);
//        refresh.setSecure(true);    // https 에서만 전송
        refresh.setPath("/");       // 모든 경로에 대하여 쿠키를 보냄
        refresh.setMaxAge(60*60*24*14);
        refresh.setAttribute("SameSite", "Strict");
        response.addCookie(refresh);

//         accessToken은 query parameter에 담아 프론트로 전달
        String url = UriComponentsBuilder.fromUriString(frontendRedirect)
                .queryParam("token", tokenPair.access())
                .build().toUriString();


        // 최종 redirect to front
        getRedirectStrategy().sendRedirect(request,response,url);

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
