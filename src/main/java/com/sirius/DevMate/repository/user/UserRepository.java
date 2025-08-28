package com.sirius.DevMate.repository.user;

import com.sirius.DevMate.domain.common.sys.OAuth2Provider;
import com.sirius.DevMate.domain.user.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
//    spring-boot-starter-data-jpa 의존성만 있으면 EntityManager가 자동 주입
    private final EntityManager em;

    /**
     * 신규면 persist, 준영속/영속이면 merge 사용
     * - @Id 가 null이면 신규로 판단
     */
    public User save(User user) {
        if (user.getUserId() == null) {    // 필드명은 엔티티에 맞게 수정 (예: getId)
            em.persist(user);
            return user;                   // persist는 그대로 영속 상태
        } else {
            return em.merge(user);         // merge는 준영속의 복제본(영속) 반환
        }
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    public Optional<User> findByEmail(String email) {
        List<User> result = em.createQuery(
                        "select u from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .setMaxResults(1)
                .getResultList();
        return result.stream().findFirst();
    }

    public Optional<User> findByProviderAndProviderId(OAuth2Provider provider, String providerId) {
        List<User> result = em.createQuery(
                        "select u from User u where u.provider = :provider and u.providerId = :providerId", User.class)
                .setParameter("provider", provider)       // Enum 그대로 바인딩
                .setParameter("providerId", providerId)
                .setMaxResults(1)
                .getResultList();
        return result.stream().findFirst();
    }

    // 닉네임 중복 체크
    public boolean existsByNickname(String nickname) {
        Long cnt = em.createQuery(
                        "select count(u) from User u where u.nickname = :nickname", Long.class)
                .setParameter("nickname", nickname)
                .getSingleResult();
        return cnt > 0;
    }

    public User findByNickname(String nickname) {
        List<User> rows = em.createQuery(
                        "select u from User u where u.nickname = :nickname", User.class)
                .setParameter("nickname", nickname)
                .setMaxResults(1)
                .getResultList();
        return rows.getFirst();
    }

    public void delete(User user) {
        em.remove(em.contains(user) ? user : em.merge(user));
    }
}
