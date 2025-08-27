package com.sirius.DevMate.config.security.oauth.service;

import com.sirius.DevMate.config.security.oauth.GithubEmailFetcher;
import com.sirius.DevMate.domain.common.sys.OAuth2Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubOAuth2UserService extends DefaultOAuth2UserService {

    private final SocialUpsertService socialUpsertService;
    private final GithubEmailFetcher githubEmailFetcher;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User user = super.loadUser(userRequest);

        Map<String, Object> attr = user.getAttributes();

        //  id는 숫자일 수 있으므로 안전하게 문자열로 변환
        Object rawId = attr.get("id");
        String providerId = (rawId == null) ? null
                : (rawId instanceof Number ? Long.toString(((Number) rawId).longValue()) : rawId.toString());
//        String providerId = (String) attr.get("id");
//        String email = (String) attr.get("email");
        String email = githubEmailFetcher.fetchPrimaryVerified(userRequest.getAccessToken().getTokenValue());
        String name = (String) attr.getOrDefault("name", attr.get("login"));
        String avatarUrl = (String) attr.get("avatar_url");

        socialUpsertService.upsert(OAuth2Provider.GITHUB, providerId, email, name, avatarUrl);

        return user;
    }
}
