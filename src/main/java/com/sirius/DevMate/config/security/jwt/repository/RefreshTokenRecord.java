package com.sirius.DevMate.config.security.jwt.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 * @param jti     키
 * @param revoked 폐기 여부
 */

public record RefreshTokenRecord(String jti, Long userId, Instant expiresAt, Boolean revoked) {
}
