package com.sirius.DevMate.config.security.controller;

import com.sirius.DevMate.config.security.jwt.repository.RefreshTokenRepository;
import com.sirius.DevMate.config.security.jwt.service.JwtTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Map;


/**
 * /auth/refresh 로 access 토큰 재발급 가능
 * refresh 만료 시 401과 함께 다시 소셜 로그인 인증을 거쳐서 토큰 pair 재발급
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
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request
            ,HttpServletResponse response
            /*@CookieValue("refresh_token") String refreshToken*/) {
        /*
        1. refresh 확인 -> null인지, 서비스에서 서명 유효성 검증
        2. DB에서 jti로 토큰 찾기
        3. 없으면 리프레시 토큰 재사용으로 판단 -> 쿠키 삭제 -> 재로그인 요청
            있으면 새 엑세스 토큰 발급 절차 수행
//        4. 만료 또는 revoke(회전) -> 토큰 revoke 처리 후 재발급 또는 쿠키 삭제
        */
        /*
        access-token은 클라이언트가 메모리 등에 관리하다
            authentication header로 보냄
        ** access 토큰 만료 시 API 서버는 상태코드로 의도를 표현(401)을 JSON으로 응답 ->
            프론트가 /auth/refresh를 호출해 새 토큰을 받아 재시도
        ** refresh 만료 시 endpoint 설계 -> /auth/logout -> redirect to / (상태로 확인)
        ** redis 조회
         */

        // 1. refresh token 존재 여부 확인
        String refreshRaw = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refresh_token"))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No Refresh Token!")
                );

        // refresh token 서명 자체의 유효성 검증
        Jwt jwt;
        try {jwt = jwtTokenService.isValidRefresh(refreshRaw);}
        catch (JwtException | BadCredentialsException e) {
            // 유효하지 않은 refresh token  -> logout
            return ResponseEntity.status(302)
                    .header("Location", "/auth/logout")
                    .build();
        }


        // 2. redis에서 refresh 토큰 만료 여부 확인
        /* refresh가 살아 있을 경우 access token 재발급과 함께 main으로 redirect */
        if (refreshTokenRepository.findByJti(jwt.getId()).isPresent()) {
            return ResponseEntity.status(302)
                    .header("Location","/main")
                    .body(Map.of("token_type","bearer",
                            "access_token",jwtTokenService.getAccessByRefresh(jwt.getSubject())));
        }
        /* refresh가 만료 됐을 경우*/
        else {
            return ResponseEntity.status(302)
                    .header("Location", "/auth/logout")
                    .build();
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "refresh_token", required = false) String refresh,
                                    HttpServletResponse response) {

        // 쿠키 제거
        var del = new Cookie("refresh_token", "");
        del.setHttpOnly(true);
        del.setPath("/"); del.setMaxAge(0);
        response.addCookie(del);
//        del.setAttribute("SameSite", "Strict");
//        del.setSecure(true);

        // 프론트는 access 메모리 삭제
        return ResponseEntity.status(302)
                .header("Location","/")
                .build();
    }
}
