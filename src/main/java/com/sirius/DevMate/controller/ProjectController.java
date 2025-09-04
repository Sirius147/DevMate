package com.sirius.DevMate.controller;

import com.sirius.DevMate.controller.dto.request.*;
import com.sirius.DevMate.controller.dto.response.*;
import com.sirius.DevMate.domain.join.Review;
import com.sirius.DevMate.exception.ProjectException;
import com.sirius.DevMate.exception.UserNotFound;
import com.sirius.DevMate.service.join.ApplicationService;
import com.sirius.DevMate.service.join.MembershipService;
import com.sirius.DevMate.service.project.ProjectService;
import com.sirius.DevMate.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-project")
public class ProjectController {

    private final UserService userService;
    private final ProjectService projectService;
    private final MembershipService membershipService;
    private final ApplicationService applicationService;

    @ResponseBody
    @GetMapping("/recruiting")
    public PageList<RecruitingProjectResponseDto> getRecruitingProject() throws UserNotFound {
        return projectService.showRecruitingProjects();
    }

    @ResponseBody
    @GetMapping("/recruiting/{projectId}/applications")
    public PageList<RecruitingProjectApplicationsDto> getRecruitingProjectApplications(
            @PathVariable("projectId") Long projectId) throws ProjectException {
        return applicationService.getApplicationsByProjectId(projectId);
    }

    @ResponseBody
    @PatchMapping("/recruiting/{projectId}/applications/{applicationId}")
    public ResponseEntity<Void> acceptOrRejectApplication(
            @PathVariable("projectId") Long projectId,
            @PathVariable("applicationId") Long applicationId,
            @RequestParam Boolean accept
    ) throws ProjectException, UserNotFound {

        applicationService.processApplication(projectId, applicationId, accept);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/recruiting/{projectId}/start-project")
    public ResponseEntity<?> recruitingEndProjectStart(@PathVariable("projectId") Long projectId) {
        projectService.endRecruitingStartProject(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/applied-project")
    public PageList<ApplicationResponseDto> getAppliedProject() throws UserNotFound {
        return applicationService.getApplicationsByUserId();
    }

    @DeleteMapping("/applied-project/{applicationId}")
    public ResponseEntity<Void> deleteApplication(@PathVariable("applicationId") Long applicationId) throws UserNotFound {
        applicationService.deleteApplicationByApplicationId(applicationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/participation")
    public PageList<ParticipatingProjectResponseDto> getParticipatingProjects() throws UserNotFound {
        return userService.showParticipatingProjects();
    }

    @GetMapping("/participation/{projectId}/chat-channel")
    public void getChatChannel() {
        return;
    }

    @GetMapping("/participation/{projectId}/docs")
    public PageList<DocDto> getProjectDoc(@PathVariable("projectId") Long projectId) {
        return projectService.getDoc(projectId);
    }

    @PostMapping("/participation/{projectId}/docs")
    public ResponseEntity<Void> newProjectDoc(
            @PathVariable("projectId") Long projectId,
            @RequestBody NewDocRequestDto newDocRequestDto) {
        projectService.createNewDoc(projectId, newDocRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/participation/{projectId}/docs/{docId}")
    public ResponseEntity<Void> updateProjectDoc(
            @PathVariable("docId") Long docId,
            @RequestBody UpdateDocRequestDto updateDocRequestDto
    ) {
        Map<String, Object> requestMapper = updateDocRequestDto.toMap();
        projectService.updateDoc(docId, requestMapper);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/participation/{projectId}/docs/{docId}")
    public ResponseEntity<Void> deleteProjectDoc(@PathVariable("projectId") Long projectId, @PathVariable("docId") Long docId) {
        projectService.deleteDoc(projectId, docId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/participation/{projectId}/todoLists")
    public PageList<TodoListDto> getProjectTodoList(@PathVariable("projectId") Long projectId) {
        return projectService.getTodoList(projectId);
    }

    @PostMapping("/participation/{projectId}/todoLists")
    public ResponseEntity<?> newProjectTodoList(@PathVariable("projectId") Long projectId,
                                                @RequestBody NewToDoListRequestDto newToDoListRequestDto) {
        projectService.createNewToDoList(projectId, newToDoListRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/participation/{projectId}/todoLists/{todoListId}")
    public ResponseEntity<?> updateProjectTodoList(
            @PathVariable("todoListId") Long todoListId,
            @RequestBody UpdateTodoListRequestDto updateTodoListRequestDto
    ) {
        Map<String, Object> map = updateTodoListRequestDto.toMap();
        projectService.updateTodoList(todoListId, map);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/participation/{projectId}/todoLists/{todoListId}")
    public ResponseEntity<?> deleteProjectTodoList(
            @PathVariable("projectId") Long projectId,
            @PathVariable("todoListId") Long todoListId) {
        projectService.deleteTodoList(projectId, todoListId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/participation/{projectId}/complete")
    public ResponseEntity<?> completeProject(@PathVariable("projectId") Long projectId) {
        projectService.completeProject(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/completed")
    public PageList<CompletedProjectResponseDto> getCompletedProjects() throws UserNotFound {
        return userService.showCompletedProjects();
    }

    @GetMapping("/completed/{projectId}/review")
    public PageList<ReviewResponseDto> getPeerReview(@PathVariable("projectId") Long projectId) {
        return projectService.getPeerReview(projectId);
    }

    @PostMapping("/completed/{projectId}/review")
    public ResponseEntity<?> peerReview(
            @PathVariable("projectId") Long projectId,
            @RequestBody ReviewContentDto reviewContentDto) throws UserNotFound {
        projectService.peerReview(projectId, reviewContentDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
