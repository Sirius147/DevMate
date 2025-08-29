package com.sirius.DevMate.config.security.oauth2.tool;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class GithubEmailFetcher {
    private static final String EMAIL_API = "https://api.github.com/user/emails";
    private final RestTemplate rest = new RestTemplate();

    public String fetchPrimaryVerified(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);                   // <1> OAuth2 액세스 토큰 인증
        headers.setAccept(List.of(MediaType.APPLICATION_JSON)); // <2> JSON 응답 원함 (Accept 헤더)

        ResponseEntity<List> resp = rest.exchange(            // <3> GET /user/emails 호출
                EMAIL_API, HttpMethod.GET,
                new HttpEntity<>(headers),
                List.class);

        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) return null; // <4> 실패/빈 응답 방어

        // <5> 응답 예시(배열):
        // [
        //   {"email":"octocat@github.com","primary":true,"verified":true,"visibility":"public"},
        //   {"email":"octo@personal.com","primary":false,"verified":true,"visibility":null}
        // ]

        // 1순위: primary && verified
        for (Object obj : resp.getBody()) {
            Map m = (Map) obj;
            Boolean primary = (Boolean) m.get("primary");
            Boolean verified = (Boolean) m.get("verified");
            if (Boolean.TRUE.equals(primary) && Boolean.TRUE.equals(verified)) {
                return (String) m.get("email");               // <6> 최우선 후보 반환
            }
        }
        // 2순위: verified (primary 아니어도 검증된 메일이면 허용)
        for (Object obj : resp.getBody()) {
            Map m = (Map) obj;
            if (Boolean.TRUE.equals(m.get("verified"))) {
                return (String) m.get("email");               // <7> 차선 후보 반환
            }
        }
        return null;                                          // <8> 적합한 메일이 없으면 null
    }
}
