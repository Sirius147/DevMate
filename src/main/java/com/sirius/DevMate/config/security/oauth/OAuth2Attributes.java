package com.sirius.DevMate.config.security.oauth;

import com.sirius.DevMate.domain.common.sys.OAuth2Provider;

import java.util.Map;

public record OAuth2Attributes(
        OAuth2Provider provider,
        String providerId,
        String email,
        String name,
        String avatarUrl
) {
    public static OAuth2Attributes of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "google" -> fromGoogle(attributes);
            case "github" -> fromGithub(attributes);
            default -> throw new IllegalArgumentException("Unsupported provider: " + registrationId);
        };
    }

    private static OAuth2Attributes fromGoogle(Map<String, Object> a) {
        String sub     = (String) a.get("sub");       // 고유 식별자
        String email   = (String) a.get("email");
        String name    = (String) a.get("name");
        String picture = (String) a.get("picture");
        return new OAuth2Attributes(OAuth2Provider.GOOGLE, sub, email, name, picture);
    }

    private static OAuth2Attributes fromGithub(Map<String, Object> a) {
        String id      = String.valueOf(a.get("id")); // 숫자일 수 있어 문자열화
        String login   = (String) a.get("login");
        String name    = (String) a.getOrDefault("name", login);
        String avatar  = (String) a.get("avatar_url");
        String email   = (String) a.get("email");     // null 가능(다음 단계에서 보강)

        return new OAuth2Attributes(OAuth2Provider.GITHUB, id, email, name, avatar);
    }
}
