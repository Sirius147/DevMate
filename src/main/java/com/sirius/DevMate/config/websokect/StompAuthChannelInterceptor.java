package com.sirius.DevMate.config.websokect;

import com.sirius.DevMate.config.security.jwt.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    /**
     * STOMP CONNECT 프레임에서 JWT 검증 → Principal 세팅
     *
     * 이렇게 하면 @MessageMapping에서 Principal로 userId 꺼낼 수 있음
     */

    private final JwtTokenService jwtTokenService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor acc = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (acc == null) return message;

        if (StompCommand.CONNECT.equals(acc.getCommand())) {
            String token = (String) acc.getSessionAttributes().get("JWT_TOKEN");
            if (token == null) {
                String auth = acc.getFirstNativeHeader("Authorization");
                if (auth != null && auth.startsWith("Bearer ")) token = auth.substring(7);
            }
            if (token == null) throw new IllegalArgumentException("Missing token");

            Long userId = jwtTokenService.getUserIdFromAccess(token);

            acc.setUser(new UserIdPrincipal(String.valueOf(userId)));
        }
        return message;
        
        }

    static final class UserIdPrincipal implements Principal {

        private final String name;

        UserIdPrincipal(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
