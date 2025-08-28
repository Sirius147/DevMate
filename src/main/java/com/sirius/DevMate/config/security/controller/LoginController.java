package com.sirius.DevMate.config.security.controller;

import com.sirius.DevMate.config.security.controller.dto.BasicSetUpDto;
import com.sirius.DevMate.config.security.controller.dto.DetailSetUpDto;
import com.sirius.DevMate.config.security.controller.dto.NicknameAvailableResponseDto;
import com.sirius.DevMate.config.security.controller.dto.StackSetUpDto;
import com.sirius.DevMate.domain.user.User;
import com.sirius.DevMate.exception.NotFoundUser;
import com.sirius.DevMate.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/")
    public String home() {
        return "index"; // src/main/resources/templates/index.html
    }

    @GetMapping("/login/basic")
    public String loginBasic(Model model, @AuthenticationPrincipal OAuth2User principal) throws NotFoundUser {
        if (principal != null) {
            User user = userService.getUser();

            model.addAttribute("authenticated", true);
            model.addAttribute("name", user.getName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("avatar", user.getAvatarUrl());

//        return "login/Basic";
            return "login";
        } else {
            log.info("principal로부터 등록 실패");
            model.addAttribute("authenticated", false);
//        return "login/Basic";
            return "login";
        }
    }

    @ResponseBody
    @PostMapping("/login/basic")
    public NicknameAvailableResponseDto loginBasicSetUp(@Valid BasicSetUpDto basicSetUpDto) throws NotFoundUser {

        // 닉네임 사용 가능
        if (userService.nickNameChecker(basicSetUpDto.getNickname())) {
            userService.setBasic(basicSetUpDto);
            return new NicknameAvailableResponseDto(true);
        }
        else {
            return new NicknameAvailableResponseDto(false);
        }
    // redirect 전략
    /*  1.      return "redirect:/login/detail";
    //  2.      HttpServletResponse response.sendRedirect("/login/detail");
    //        return ResponseEntity.
    //                status(302)
    //                .header("Location", "/login/detail")
    //                .build(); */
    }

    @GetMapping("/login/detail")
    public String loginDetail() {
        return "login/detail";
    }

    @PostMapping("/login/detail")
    public ResponseEntity<Void> loginDetailSetUp(@Valid DetailSetUpDto detailSetUpDto) throws NotFoundUser {
        userService.setDetail(detailSetUpDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/login/stack")
    public String loginStack() {
        return "/login/stack";
    }

    @PostMapping("/login/stack")
    public ResponseEntity<Void> loginStackSetUp(@RequestBody @Valid List<StackSetUpDto> stackSetUpDtoList) throws NotFoundUser {
        userService.setStack(stackSetUpDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/main")
    public String mainPage() {
        return "mainPage";
    }

//    // 미인가 사용자가 접근 시 mapping
//    @GetMapping("/login")
//    public String login(Model model, @AuthenticationPrincipal OAuth2User principal) {
//        if (principal != null) {
//            // provider별 키 차이를 흡수 (google: name/picture/email, github: login/avatar_url/email?)
//            String name = (String) Optional.ofNullable(principal.getAttribute("name"))
//                    .orElse(principal.getAttribute("login"));
//            String email = principal.getAttribute("email"); // 깃허브는 null일 수 있음
//            log.info("소셜 이메일: {}", Optional.ofNullable(principal.getAttribute("email")));
//            String avatar = (String) Optional.ofNullable(principal.getAttribute("picture"))
//                    .orElse(principal.getAttribute("avatar_url"));
//
//            model.addAttribute("authenticated", true);
//            model.addAttribute("name", name);
//            model.addAttribute("email", email);
//            model.addAttribute("avatar", avatar);
//
//            return "login";
//        } else {
//            log.info("principal로부터 등록 실패");
//            model.addAttribute("authenticated", false);
//            return "login";
//        }
//
//    }

}
