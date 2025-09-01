package com.sirius.DevMate.controller;

import com.sirius.DevMate.config.security.controller.dto.response.NicknameAvailableResponseDto;
import com.sirius.DevMate.controller.dto.request.NewProjectDto;
import com.sirius.DevMate.controller.dto.request.PageRequestDto;
import com.sirius.DevMate.controller.dto.request.ProjectSearchRequestDto;
import com.sirius.DevMate.controller.dto.request.UpdateProfileDto;
import com.sirius.DevMate.controller.dto.response.*;
import com.sirius.DevMate.domain.common.project.Position;
import com.sirius.DevMate.domain.project.Project;
import com.sirius.DevMate.exception.UserNotFound;
import com.sirius.DevMate.exception.ProjectException;
import com.sirius.DevMate.service.project.ProjectService;
import com.sirius.DevMate.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {


    private final ProjectService projectService;
    private final UserService userService;

    @ResponseBody
    @GetMapping
    public MainResponseDto mainPage(@RequestParam Integer page) throws UserNotFound {
        return new MainResponseDto(
                projectService.showAllProjects(new PageRequestDto(page, "updatedAt")),
                userService.notifyUserNotifications());
    }

    @ResponseBody
    @GetMapping("/projects")
    public PageList<Project> searchProject(@RequestBody ProjectSearchRequestDto projectSearchRequestDto) {
        return projectService.searchProject(projectSearchRequestDto);
    }

    @PostMapping("/apply/{projectId}")
    public ResponseEntity<Void> applyProject(@PathVariable("projectId") Long projectId,
                                             @RequestParam Position position,
                                             @RequestParam String content) throws ProjectException, UserNotFound {
        projectService.newApplication(projectId, position, content);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/my-page")
    public MyPageDto myPage() throws UserNotFound {
        return userService.getMyPage();
    }

    @PostMapping("/my-page")
    public ResponseEntity<NicknameAvailableResponseDto> checkNickname(@RequestParam String newNickname) {
        if (userService.isNickNameAvailable(newNickname)) {
            return ResponseEntity.status(201)
                    .body(new NicknameAvailableResponseDto(true));
        } else {
            return ResponseEntity.status(201)
                    .body(new NicknameAvailableResponseDto(false));
        }
    }

    @PostMapping("/my-page")
    public ResponseEntity<Void> updateProfile(@RequestBody UpdateProfileDto updateProfileDto) throws UserNotFound {
        userService.setProfile(updateProfileDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/notifications")
    public PageList<NotificationDto> notifications() throws UserNotFound {
        return userService.getNotification();
    }

    @ResponseBody
    @GetMapping("/my-project")
    public List<MembershipDto> myProject() throws UserNotFound {
        return userService.getMemberShips();
    }

    @GetMapping("/new-project")
    public ResponseEntity<Void> newProject() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/new-project")
    public ResponseEntity<Void> makeProject(@RequestBody NewProjectDto newProjectDto) throws UserNotFound {
        projectService.newProject(newProjectDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
