package com.sirius.DevMate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
public class LoginController {
    @GetMapping("/")
    public String home() {
        return "index"; // src/main/resources/templates/index.html
    }

    // 미인가 사용자가 접근 시 mapping
    @GetMapping("/login")
    public String login(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            // provider별 키 차이를 흡수 (google: name/picture/email, github: login/avatar_url/email?)
            String name = (String) Optional.ofNullable(principal.getAttribute("name"))
                    .orElse(principal.getAttribute("login"));
            String email = principal.getAttribute("email"); // 깃허브는 null일 수 있음
            log.info("소셜 이메일: {}", Optional.ofNullable(principal.getAttribute("email")));
            String avatar = (String) Optional.ofNullable(principal.getAttribute("picture"))
                    .orElse(principal.getAttribute("avatar_url"));

            model.addAttribute("authenticated", true);
            model.addAttribute("name", name);
            model.addAttribute("email", email);
            model.addAttribute("avatar", avatar);

            return "login";
        } else {
            log.info("principal로부터 등록 실패");
            model.addAttribute("authenticated", false);
            return "login";
        }
        // 최초 로그인 유저 시 초기프로필 설정 페이지로 이동

        // 기존 유저 로그인 시 main 페이지로 이동

        // 미인증시 로그인 페이지로 이동
//        return "login";
    }

    @GetMapping("/me")
    @ResponseBody
    Object me(@AuthenticationPrincipal OAuth2User user) {
        return (user == null) ? Map.of("authenticated", false)
                : Map.of("authenticated", true, "attrs", user.getAttributes());
    }
//    @GetMapping("/secure")
//    @ResponseBody
//    public String secure(@AuthenticationPrincipal OAuth2User principal) {
//        return "Hello, " + (principal != null ? principal.getAttributes().getOrDefault("email","user") : "anonymous");
//    }
}
