package com.sirius.DevMate.config.security.service;

import com.sirius.DevMate.domain.common.sys.OAuth2Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final SocialUpsertService socialUpsertService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUserService delegate = new OidcUserService();
        OidcUser oidcUser = delegate.loadUser(userRequest);

        String providerId = oidcUser.getSubject(); // google 고유 sub
        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();
        String avatarUrl = oidcUser.getPicture();

        socialUpsertService.upsert(OAuth2Provider.GOOGLE, providerId, email, name, avatarUrl);
        log.info("Google OIDC upsert 완료 - sub={}, email={}", providerId, email);

        // 5) SecurityContext에 올릴 OidcUser 구성
        return oidcUser;
    }
}
