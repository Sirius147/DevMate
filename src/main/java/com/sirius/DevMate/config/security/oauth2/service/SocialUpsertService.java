package com.sirius.DevMate.config.security.oauth2.service;

import com.sirius.DevMate.domain.common.sys.OAuth2Provider;
import com.sirius.DevMate.domain.user.User;
import com.sirius.DevMate.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SocialUpsertService {

    private final UserRepository userRepository;

    @Transactional
    public void upsert(OAuth2Provider provider, String providerId, String email,
                       String name, String avatarUrl) {

        // 1) provider + providerId 로 1차 매칭
        Optional<User> byProvider = userRepository
                .findByProviderAndProviderId(provider, providerId);

        User user = byProvider.orElseGet(() ->
                // 없으면
                Optional.ofNullable(email)
                        .flatMap(userRepository::findByEmail)
                        .orElseGet(() -> User.fromOAuth(provider, providerId,email, name, avatarUrl))  // ★ Builder로 생성
        );

        // 기존 계정이면 동기화
        if (user.getUserId() != null) {
            user.syncFromOAuth(name, avatarUrl, email);
            // provider/providierId 최신화 (동일 값이지만 명시적으로 유지)
            user = user.toBuilder()
                    .provider(provider)
                    .providerId(providerId)
                    .build();
        }

        userRepository.save(user);

    }
}
