package com.sirius.DevMate.config.security.controller;

import com.sirius.DevMate.config.security.controller.dto.request.BasicSetUpDto;
import com.sirius.DevMate.config.security.controller.dto.request.DetailSetUpDto;
import com.sirius.DevMate.config.security.controller.dto.response.NicknameAvailableResponseDto;
import com.sirius.DevMate.config.security.controller.dto.request.StackSetUpDto;
import com.sirius.DevMate.domain.user.User;
import com.sirius.DevMate.exception.UserNotFound;
import com.sirius.DevMate.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

    // login.html 파일 로드
    @GetMapping("/login/basic")
    public String loginBasic(Model model, @AuthenticationPrincipal Jwt principal) throws UserNotFound {
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
    public NicknameAvailableResponseDto loginBasicSetUp(@Valid @RequestBody BasicSetUpDto basicSetUpDto) throws UserNotFound {

        // 닉네임 사용 가능
        if (userService.isNickNameAvailable(basicSetUpDto.getNickname())) {
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
    public ResponseEntity<Void> loginDetailSetUp(@Valid @RequestBody DetailSetUpDto detailSetUpDto) throws UserNotFound {
        userService.setDetail(detailSetUpDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/login/stack")
    public String loginStack() {
        return "/login/stack";
    }

    @PostMapping("/login/stack")
    public ResponseEntity<Void> loginStackSetUp(@RequestBody @Valid List<StackSetUpDto> stackSetUpDtoList) throws UserNotFound {
        userService.setStack(stackSetUpDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
