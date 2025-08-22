package com.sirius.DevMate;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal OAuth2User principal) {
        model.addAttribute("me", principal);
        return "index"; // src/main/resources/templates/index.html
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // 로그인 커스텀하면 템플릿 추가
    }

    @GetMapping("/secure")
    @ResponseBody
    public String secure(@AuthenticationPrincipal OAuth2User principal) {
        return "Hello, " + (principal != null ? principal.getAttributes().getOrDefault("email","user") : "anonymous");
    }
}
