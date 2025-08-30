package com.sirius.DevMate.config.security.jwt.service;

import com.sirius.DevMate.config.security.jwt.repository.RefreshTokenRecord;
import com.sirius.DevMate.config.security.jwt.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.access-exp-seconds}")
    private Long accessExpSeconds;
    @Value("${jwt.refresh-exp-seconds}")
    private Long refreshExpSeconds;

    public record TokenPair(String access, String refresh){};

    public TokenPair issue(Long userId, String roles) {
        String access = createAccessToken(userId, roles, accessExpSeconds);
        String refresh = createRefreshToken(userId, refreshExpSeconds);

        return new TokenPair(access, refresh);
    }

    public String getAccessByRefresh(String userId ) {
        return createAccessToken(Long.valueOf(userId), "USER", accessExpSeconds);
    }

    private String createAccessToken(Long userId, String roles, Long accessExpSeconds) {
        Instant now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(accessExpSeconds))
                .subject(String.valueOf(userId))
                .claim("roles", roles)
                .claim("type", "access")
                .build();
        var header = JwsHeader.with(MacAlgorithm.HS256)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    private String createRefreshToken(Long userId, Long refreshExpSeconds) {
        Instant now = Instant.now();
        String jti = newJti();
        var claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(refreshExpSeconds))
                .subject(String.valueOf(userId))
                .claim("jti", jti)
                .claim("type", "refresh")
                .build();
        var header = JwsHeader.with(MacAlgorithm.HS256)
                .build();

        refreshTokenRepository.save(new RefreshTokenRecord(jti, userId, now.plusSeconds(refreshExpSeconds)));

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

    }

    private String newJti() {
        return UUID.randomUUID().toString();
    }

//    public String createToken(Long userId, String roles, Long expSeconds, String type) {
//
//        var now = Instant.now();
//        var claims = JwtClaimsSet.builder()
//                .issuer("devmate")
//                .issuedAt(now)
//                .expiresAt(now.plusSeconds(expSeconds))
//                .subject(String.valueOf(userId))
//                .claim("roles", roles)
//                .claim("type", type)
//                .build();
//        var header = JwsHeader.with(MacAlgorithm.HS256)
//                .build();
//
//        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
//    }

    public Jwt isValidRefresh(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        if ("refresh".equals(jwt.getClaimAsString("type"))) {
            return jwt;
        } else {
            throw new BadCredentialsException("Invalid refresh Token 입니다");
        }
    }

}
