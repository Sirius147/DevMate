package com.sirius.DevMate.config.security.controller;

import com.sirius.DevMate.config.security.jwt.service.JwtTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * /auth/refresh 로 access 토큰 재발급 가능
 * refresh 만료 시 401과 함께 다시 소셜 로그인 인증을 거쳐서 토큰 pair 재발급
 * AuthController가 그 과정을 진행
 *
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access-exp-seconds}")
    private String accessExpSeconds;
    private final JwtTokenService jwtTokenService;
    private final JwtDecoder jwtDecoder;

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("refresh_token") String refreshToken) {
        // 검증: 타입=refresh, 만료X, DB화이트리스트/회전 체크
        if (!jwtTokenService.isRefresh(refreshToken)) return ResponseEntity.status(401).build();

        // 보통 refresh에는 userId만 실어두고, DB 조회해 최신 권한으로 새 access 발급
        var jwt = jwtDecoder.decode(refreshToken);
        Long userId = Long.valueOf(jwt.getSubject());
        String role = String.valueOf(jwt.getClaim("roles"));


        JwtTokenService.TokenPair pair = jwtTokenService.issue(userId, role); // 회전 로직 포함 권장
        // 새 refresh 토큰 쿠키 교체(회전)
        Cookie c = new Cookie("refresh_token", pair.refresh());
        c.setHttpOnly(true);
//        c.setSecure(true);
        c.setPath("/");
        c.setMaxAge(60*60*24*14);
        c.setAttribute("SameSite","Strict");
        return ResponseEntity
                .ok(Map.of("accessToken"
                        , pair.access()
                        , "tokenType"
                        , "Bearer"
                        , "expiresIn", accessExpSeconds));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "refresh_token", required = false) String refresh,
                                    HttpServletResponse res) {

        // 쿠키 제거
        var del = new Cookie("refresh_token", "");
        del.setHttpOnly(true);
//        del.setSecure(true);
        del.setPath("/"); del.setMaxAge(0);
        del.setAttribute("SameSite", "Strict");
        res.addCookie(del);

        // 프론트는 access 메모리 삭제
        return ResponseEntity.ok().build();
    }
}
