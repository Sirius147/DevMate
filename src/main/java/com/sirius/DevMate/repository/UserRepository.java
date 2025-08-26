package com.sirius.DevMate.repository;

import com.sirius.DevMate.domain.common.sys.OAuth2Provider;
import com.sirius.DevMate.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByProviderAndProviderId(OAuth2Provider oAuth2Provider, String providerId);
}
