package com.sirius.DevMate.config.security.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${jwt.access-exp-seconds}")
    private Long accessExpSeconds;
    @Value("${jwt.refresh-exp-seconds}")
    private Long refreshExpSeconds;

    public record TokenPair(String access, String refresh){};

    public TokenPair issue(Long userId, String roles) {
        String access = createToken(userId, roles, accessExpSeconds, "access");
        String refresh = createToken(userId, roles, refreshExpSeconds, "refresh");

        return new TokenPair(access, refresh);
    }

    public String createToken(Long userId, String roles, Long expSeconds, String type) {

        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer("devmate")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expSeconds))
                .subject(String.valueOf(userId))
                .claim("roles", roles)
                .claim("type", type)
                .build();
        var header = JwsHeader.with(MacAlgorithm.HS256)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public boolean isRefresh(String token) {
        var jwt = jwtDecoder.decode(token);
        return "refresh".equals(jwt.getClaimAsString("type"));
    }

}
