package com.sirius.DevMate.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // 최초 로그인 여부에 따라 분기하고 싶으면, SecurityContext의 principal/email로 DB 조회
        // boolean needsProfile = ...
        // String target = needsProfile ? "/profile/setup" : "/";
        String target = "/"; // 일단 홈으로
        response.sendRedirect(target);
    }
}
