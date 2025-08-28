package com.sirius.DevMate.config.security.handler;

import com.sirius.DevMate.domain.common.sys.OAuth2Provider;
import com.sirius.DevMate.domain.user.User;
import com.sirius.DevMate.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException  {

        // 기본 SavedRequest 리다이렉트를 무시하고 목적지를 직접 정함
        setAlwaysUseDefaultTargetUrl(true);

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String registrationId = token.getAuthorizedClientRegistrationId(); // "google" | "github"

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

        if (opt.isEmpty()) {
            // 정말 드물지만, upsert 전에 핸들러가 먼저 불리면 방어적으로 메인으로
            log.error("upsert 전에 successHandler가 먼저 호출!");
            getRedirectStrategy().sendRedirect(request, response, "/main");
            return;
        }

        User user = opt.get();
        // 최초 로그인 유저
        if (user.getCreatedAt().equals(user.getUpdatedAt())) {
            getRedirectStrategy().sendRedirect(request, response, "/login/basic");
        } else { // 기존 로그인 유저
            getRedirectStrategy().sendRedirect(request, response, "/main");
        }
    }
}
