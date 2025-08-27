package com.sirius.DevMate.config.security.controller;

import com.sirius.DevMate.config.security.controller.dto.BasicSetUpDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Slf4j
@Controller
public class LoginController {

    @GetMapping("/")
    public String home() {
        return "index"; // src/main/resources/templates/index.html
    }

    @GetMapping("/login/basic")
    public String loginBasic() {
        return "login/Basic";
    }

    @PostMapping("/login/basic")
    public ResponseEntity<Void> loginBasicSetUp(
            BasicSetUpDto basicSetUpDto,
            @AuthenticationPrincipal OAuth2User principal) {
        // 인가 유저 db에서 find 하고 update 하기
        // service에 메서드 하나 만들기 ?

//  1.      return "redirect:/login/detail";
//  2.      HttpServletResponse response.sendRedirect("/login/detail");
        return ResponseEntity.
                status(302)
                .header("Location", "/login/detail")
                .build();
    }

    @GetMapping("/login/detail")
    public String loginDetail() {
        return "login/detail";
    }


    @GetMapping("/main")
    public String mainPage() {
        return "mainPage";
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

    }

//    @GetMapping("/me")
//    @ResponseBody
//    Object me(@AuthenticationPrincipal OAuth2User user) {
//        return (user == null) ? Map.of("authenticated", false)
//                : Map.of("authenticated", true, "attrs", user.getAttributes());
//    }

}
