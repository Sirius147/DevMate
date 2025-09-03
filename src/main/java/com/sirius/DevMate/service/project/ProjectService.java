package com.sirius.DevMate.service.project;

import com.sirius.DevMate.controller.dto.response.RecruitingProjectResponseDto;
import com.sirius.DevMate.controller.dto.request.NewProjectDto;
import com.sirius.DevMate.controller.dto.response.PageList;
import com.sirius.DevMate.controller.dto.request.PageRequestDto;
import com.sirius.DevMate.controller.dto.request.ProjectSearchRequestDto;
import com.sirius.DevMate.domain.common.project.MembershipRole;
import com.sirius.DevMate.domain.common.project.MembershipStatus;
import com.sirius.DevMate.domain.join.Membership;
import com.sirius.DevMate.domain.project.Project;
import com.sirius.DevMate.domain.user.User;
import com.sirius.DevMate.exception.ProjectException;
import com.sirius.DevMate.exception.UserNotFound;
import com.sirius.DevMate.repository.user.NotificationRepository;
import com.sirius.DevMate.repository.join.ApplicationRepository;
import com.sirius.DevMate.repository.join.MembershipRepository;
import com.sirius.DevMate.repository.project.ProjectRepository;
import com.sirius.DevMate.service.join.MembershipService;
import com.sirius.DevMate.service.user.NotificationService;
import com.sirius.DevMate.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MembershipRepository membershipRepository;
    private final ApplicationRepository applicationRepository;
    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final MembershipService membershipService;
    private static final Integer LIST_SIZE = 10;

    public void newProject(NewProjectDto newProjectDto) throws UserNotFound, ProjectException {

        Integer recruitSize = newProjectDto.backendMembers() + newProjectDto.frontendMembers() +
                newProjectDto.pmMembers() + newProjectDto.designMembers();

        Integer currentSize = newProjectDto.currentBackend() + newProjectDto.currentFrontend() +
                newProjectDto.currentDesign() + newProjectDto.currentPm();


        if (newProjectDto.backendMembers() - newProjectDto.currentBackend() <= 0) {
            throw new ProjectException("현재 백엔드 인원이 전체 백엔드 인원보다 많습니다!");
        }
        if (newProjectDto.frontendMembers() - newProjectDto.currentFrontend() <= 0) {
            throw new ProjectException("현재 프론트 인원이 전체 프론트 인원보다 많습니다!");
        }
        if (newProjectDto.designMembers() - newProjectDto.currentDesign() <= 0) {
            throw new ProjectException("현재 디자인 인원이 전체 디자인 인원보다 많습니다!");
        }
        if (newProjectDto.pmMembers() - newProjectDto.currentPm() <= 0) {
            throw new ProjectException("현재 pm 인원이 전체 pm 인원보다 많습니다!");
        }

        Project newProject = Project
                .builder()
                .title(newProjectDto.title())
                .shortDescription(newProjectDto.shortDescription())
                .recruitSize(recruitSize)
                .currentSize(currentSize)
                .startDate(newProjectDto.startDate())
                .endDate(newProjectDto.endDate())
                .collaborateStyle(newProjectDto.collaborateStyle())
                .preferredRegion(newProjectDto.preferredRegion())
                .projectLevel(newProjectDto.projectLevel())
                .backendMembers(newProjectDto.backendMembers())
                .currentBackend(newProjectDto.currentBackend())
                .frontendMembers(newProjectDto.frontendMembers())
                .currentFrontend(newProjectDto.currentFrontend())
                .designMembers(newProjectDto.designMembers())
                .currentDesign(newProjectDto.currentDesign())
                .pmMembers(newProjectDto.pmMembers())
                .currentPm(newProjectDto.currentPm())
                .build();

        Membership newMembership = membershipService.createMembership(
                userService.getUser(),
                newProject,
                MembershipRole.LEADER,
                MembershipStatus.PARTICIPATION
                );

        projectRepository.save(newProject);

    }

    public Project getProjectById(Long id) {
         return projectRepository.findById(id);
    }

    public List<Membership> getMemberships(Project project) {
        return membershipRepository.findByProjectId(project.getProjectId());
    }

    public PageList<Project> showAllProjects(PageRequestDto pageRequestDto) {
        return projectRepository.findAll(pageRequestDto.page(),LIST_SIZE, pageRequestDto.sortBy());
    }

    public PageList<Project> searchProject(ProjectSearchRequestDto projectSearchConditionDto) {

        return projectRepository.findByCondition(
                projectSearchConditionDto.getPage(),
                LIST_SIZE,
                "updatedAt",
                projectSearchConditionDto.getRegions(),
                projectSearchConditionDto.getPreferredAtmosphere(),
                projectSearchConditionDto.getSkillLevel(),
                projectSearchConditionDto.getProjectStatus());
    }

    public PageList<RecruitingProjectResponseDto> showRecruitingProjects() throws UserNotFound {
        User loginUser = userService.getUser();
        List<Membership> leaderMemberships = membershipService.getLeaderMembership(loginUser);
        if (leaderMemberships.isEmpty()) {
            return new PageList<>(null, 0L);
        } else {

            List<RecruitingProjectResponseDto> recruitingProjectResponseDtos = new ArrayList<>();
            for (Membership membership : leaderMemberships) {
                recruitingProjectResponseDtos.add(
                        new RecruitingProjectResponseDto(
                                membership.getProject().getProjectId(),
                                membership.getProject().getTitle(),
                                membership.getProject().getRecruitSize(),
                                membership.getProject().getCurrentSize(),
                                membership.getProject().getBackendMembers(),
                                membership.getProject().getCurrentBackend(),
                                membership.getProject().getFrontendMembers(),
                                membership.getProject().getCurrentFrontend(),
                                membership.getProject().getDesignMembers(),
                                membership.getProject().getCurrentDesign(),
                                membership.getProject().getPmMembers(),
                                membership.getProject().getCurrentPm(),
                                membership.getProject().getProjectStatus()
                        ));

            }

            return new PageList<>(recruitingProjectResponseDtos, (long) leaderMemberships.size());
        }
    }
}
