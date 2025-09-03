package com.sirius.DevMate.controller;

import com.sirius.DevMate.controller.dto.response.ApplicationResponseDto;
import com.sirius.DevMate.controller.dto.response.PageList;
import com.sirius.DevMate.controller.dto.response.RecruitingProjectApplicationsDto;
import com.sirius.DevMate.controller.dto.response.RecruitingProjectResponseDto;
import com.sirius.DevMate.exception.ProjectException;
import com.sirius.DevMate.exception.UserNotFound;
import com.sirius.DevMate.service.join.ApplicationService;
import com.sirius.DevMate.service.join.MembershipService;
import com.sirius.DevMate.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-project")
public class ProjectController {

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

    @GetMapping("/applied-project")
    public PageList<ApplicationResponseDto> getAppliedProject() throws UserNotFound {
        return applicationService.getApplicationsByUserId();
    }





}
