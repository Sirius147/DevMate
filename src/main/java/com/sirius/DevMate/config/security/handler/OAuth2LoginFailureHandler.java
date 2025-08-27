package com.sirius.DevMate.config.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        String msg = exception.getMessage();
        // 너무 긴/내부 메시지는 잘라내거나, 고정 메시지로 바꾸는 것도 방법
        if (msg == null || msg.isBlank()) msg = "Authentication failed";
        String encoded = URLEncoder.encode(msg, StandardCharsets.UTF_8);
        getRedirectStrategy().sendRedirect(request, response, "/login?error=" + encoded);
    }
}
