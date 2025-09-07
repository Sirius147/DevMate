package com.sirius.DevMate.config.websokect;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServerHttpRequest req) {
            HttpHeaders headers = req.getHeaders();
            String auth = headers.getFirst("Authorization");
            String token = null;

            if (auth != null && auth.startsWith("Bearer ")) {
                token = auth.substring(7);
            }
//            else {
//                // 쿼리 파라미터 ?token=...
//                var uri = req.getURI();
//                var query = uri.getQuery();
//                if (query != null) {
//                    for (String part : query.split("&")) {
//                        var kv = part.split("=");
//                        if (kv.length == 2 && kv[0].equals("token")) token = kv[1];
//                    }
//                }
//            }
            if (token != null) {
                attributes.put("JWT_TOKEN", token);
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
