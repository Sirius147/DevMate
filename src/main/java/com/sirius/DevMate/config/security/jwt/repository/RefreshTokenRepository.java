package com.sirius.DevMate.config.security.jwt.repository;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;


@Repository
public class RefreshTokenRepository {

    private final StringRedisTemplate redis;
    private static final String KEY_PREFIX = "rt:";

    public RefreshTokenRepository(StringRedisTemplate redis) {
        this.redis = redis;
    }

    private String key(String jti) {
        return KEY_PREFIX + jti;
    }

    public void save(RefreshTokenRecord record) {
        String key = key(record.jti());
        Map<String, String> map = toMap(record);
        redis.opsForHash().putAll(key, map);

        // 휘발 설정
        long seconds = Math.max(1, record.expiresAt().getEpochSecond() - Instant.now().getEpochSecond());
        redis.expire(key, Duration.ofSeconds(seconds));
    }

    public Optional<RefreshTokenRecord> findByJti(String jti) {
        String key = key(jti);
        Map<Object, Object> entries = redis.opsForHash().entries(key);
        if (entries.isEmpty()) return Optional.empty();
        return Optional.of(fromMap(entries));
    }

//    public void revoke(String jti) {
//        String key = key(jti);
//        redis.opsForHash().put(key, "revoked", "1");
//        // TTL은 그대로 유지
//    }

    // 즉시 삭제
    public void delete(String jti) {
        redis.delete(key(jti));
    }


    private Map<String, String> toMap(RefreshTokenRecord r) {
        return Map.of(
                "jti", r.jti(),
                "userId", String.valueOf(r.userId()),
                "expiresAt", String.valueOf(r.expiresAt().getEpochSecond())
//                "revoked", r.revoked() ? "1" : "0"
        );
    }

    private RefreshTokenRecord fromMap(Map<Object, Object> m) {
        String jti = str(m.get("jti"));
        Long userId = Long.parseLong(str(m.get("userId")));
        Instant expiresAt = Instant.ofEpochSecond(Long.parseLong(str(m.get("expiresAt"))));
//        boolean revoked = "1".equals(str(m.get("revoked")));
        return new RefreshTokenRecord(jti, userId, expiresAt);
    }

    private static String str(Object o) {
        return o == null ? "" : o.toString();
    }
}
