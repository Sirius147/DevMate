package com.sirius.DevMate.service;

import com.sirius.DevMate.domain.common.sys.OAuthProvider;
import com.sirius.DevMate.domain.user.User;
import com.sirius.DevMate.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final RestTemplate rest = new RestTemplate();

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * - 소셜 제공자에서 프로필을 받아 우리 도메인 User로 Upsert
     * - 엔티티는 setter 대신 Builder/toBuilder를 사용
     */
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        // 1) 표준 사용자 속성 로딩
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oauthUser = delegate.loadUser(req);

        String provider = req.getClientRegistration().getRegistrationId(); // google, github
        Map<String, Object> attrs = oauthUser.getAttributes();

        // 2) 제공자별 표준화
        String providerId, email, name, pictureUrl;
        if ("google".equals(provider)) {
            providerId = String.valueOf(attrs.get("sub"));
            email      = (String) attrs.get("email");
            name       = (String) attrs.getOrDefault("name", "");
            pictureUrl = (String) attrs.getOrDefault("picture", "");
        } else if ("github".equals(provider)) {
            providerId = String.valueOf(attrs.get("id"));
            email      = (String) attrs.get("email"); // 비공개면 null
            if (email == null || email.isBlank()) {
                email = fetchGithubPrimaryEmail(req.getAccessToken().getTokenValue()); // scope: user:email 필요
            }
            name       = (String) Optional.ofNullable(attrs.get("name"))
                    .orElse(attrs.getOrDefault("login", ""));
            pictureUrl = (String) attrs.getOrDefault("avatar_url", "");
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + provider);
        }

        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException("Email not provided by " + provider);
        }

        // 3) Upsert (setter 금지 → builder/toBuilder 사용)
        // 3-1) (provider, providerId)로 찾아보고
        Optional<User> byProv = userRepository.findByProviderAndProviderId(provider, providerId);
        String finalEmail = email;
        User user = byProv.orElseGet(() -> {
            // 3-2) 없으면 email로 찾음 (동일 이메일로 다른 provider 로그인한 케이스)
            Optional<User> byEmail = userRepository.findByEmail(finalEmail);
            if (byEmail.isPresent()) {
                // 정책: 같은 이메일은 계정 연결로 간주 → provider 정보만 덮어쓰기(toBuilder)
                User linked = byEmail.get()
                        .toBuilder()
                        .provider(OAuthProvider.valueOf(provider))
                        .providerId(providerId)
                        .build();
                return userRepository.save(linked);
            }
            // 3-3) 완전 신규 → Builder로 생성
            User created = User.builder()
                    .email(finalEmail)
                    .provider(OAuthProvider.valueOf(provider))
                    .providerId(providerId)
                    .name(name)
                    .pictureUrl(pictureUrl)
                    .role("ROLE_USER")
                    .build();
            return userRepository.save(created);
        });

        // 4) 프로필 변경분이 있으면 toBuilder로 갱신 저장
        boolean needUpdate =
                !Objects.equals(user.getName(), name) ||
                        !Objects.equals(user.getPictureUrl(), pictureUrl);

        if (needUpdate) {
            User updated = user.toBuilder()
                    .name(name)
                    .pictureUrl(pictureUrl)
                    .build();
            user = userRepository.save(updated);
        }

        // 5) SecurityContext에 올릴 OAuth2User 구성
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(user.getRole()));
        String nameAttributeKey = "google".equals(provider) ? "sub" : "id";

        return new DefaultOAuth2User(authorities, attrs, nameAttributeKey);
    }

    /**
     * GitHub 이메일이 비공개인 경우 /user/emails로 primary/verified 이메일 조회
     */
    private String fetchGithubPrimaryEmail(String accessToken) {
        String url = "https://api.github.com/user/emails";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        ResponseEntity<List<Map<String, Object>>> resp = rest.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            return null;
        }

        return resp.getBody().stream()
                .filter(m -> Boolean.TRUE.equals(m.get("primary")) && Boolean.TRUE.equals(m.get("verified")))
                .map(m -> (String) m.get("email"))
                .findFirst()
                .orElseGet(() -> resp.getBody().stream()
                        .map(m -> (String) m.get("email"))
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(null));
    }
}
