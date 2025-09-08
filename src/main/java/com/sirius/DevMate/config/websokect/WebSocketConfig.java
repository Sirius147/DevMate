package com.sirius.DevMate.config.websokect;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompAuthChannelInterceptor stompAuthChannelInterceptor;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompAuthChannelInterceptor);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {


        registry.addEndpoint("/ws-chat")               // ← 순수 WS
                .setAllowedOriginPatterns("*");
        // stomp 접속 url
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new JwtHandshakeInterceptor())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 구독(메시지 수신) 경로 (prefix)
        registry.enableSimpleBroker("/topic", "/queue");
//                .setTaskScheduler(new ThreadPoolTaskScheduler())
//                .setHeartbeatValue(new long[]{10000, 10000}); // 서버 클라이언트 heartBeat 10s

        // 서버 수신(메시지 발행 송신) 경로(prefix)
        registry.setApplicationDestinationPrefixes("/app");
        // 사용자 전용 큐 prefix
        registry.setUserDestinationPrefix("/user");

    }
}
