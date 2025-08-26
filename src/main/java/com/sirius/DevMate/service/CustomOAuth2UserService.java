package com.sirius.DevMate.service;

import com.sirius.DevMate.config.security.oauth.GithubEmailFetcher;
import com.sirius.DevMate.config.security.oauth.OAuth2Attributes;
import com.sirius.DevMate.domain.common.sys.OAuth2Provider;
import com.sirius.DevMate.domain.user.User;
import com.sirius.DevMate.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final GithubEmailFetcher githubEmailFetcher;
//    private final RestTemplate rest = new RestTemplate();

    /**
     * - 소셜 제공자에서 프로필을 받아 우리 도메인 User로 Upsert
     * - 엔티티는 setter 대신 Builder/toBuilder를 사용
     */
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        // 1) 소셜에서 사용자 정보 로드
        OAuth2User oauthUser = super.loadUser(req);

        // 2) 유저 엔티티로 맵핑
        String registrationId = req.getClientRegistration().getRegistrationId(); // google, github
        var attr = OAuth2Attributes.of(registrationId, oauthUser.getAttributes());

//        String resolvedEmail = attr.email(); // github는 null 가능
        // 1) 깃허브 이메일 보강
        String resolvedEmail = attr.email();
        if (attr.provider() == OAuth2Provider.GITHUB && resolvedEmail == null) {
            resolvedEmail = githubEmailFetcher.fetchPrimaryVerified(req.getAccessToken().getTokenValue());
        } else {
            resolvedEmail = attr.email();
        }
        // 1) provider+providerId 우선 조회
        Optional<User> byProvider = userRepository.findByProviderAndProviderId(attr.provider(), attr.providerId());

        String finalResolvedEmail = resolvedEmail;
        User user = byProvider.orElseGet(() ->
                // 없으면
                Optional.ofNullable(finalResolvedEmail)
                        .flatMap(userRepository::findByEmail)
                        .orElseGet(() -> User.fromOAuth(attr, finalResolvedEmail))  // ★ Builder로 생성
        );

        // 기존 계정이면 동기화
        if (user.getUserId() != null) {
            user.syncFromOAuth(attr, resolvedEmail);
        }

        userRepository.save(user);

        // 5) SecurityContext에 올릴 OAuth2User 구성
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(user.getRole()));
        String nameAttrKey = req.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // google=sub, github=id

        return new DefaultOAuth2User(authorities, oauthUser.getAttributes(), nameAttrKey);
    }

    }

