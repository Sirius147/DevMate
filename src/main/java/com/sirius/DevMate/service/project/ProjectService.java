package com.sirius.DevMate.service.project;

import com.sirius.DevMate.controller.dto.request.NewProjectDto;
import com.sirius.DevMate.controller.dto.response.PageList;
import com.sirius.DevMate.controller.dto.request.PageRequestDto;
import com.sirius.DevMate.controller.dto.request.ProjectSearchRequestDto;
import com.sirius.DevMate.domain.common.project.ApplicationStatus;
import com.sirius.DevMate.domain.common.project.MembershipRole;
import com.sirius.DevMate.domain.common.project.MembershipStatus;
import com.sirius.DevMate.domain.common.project.Position;
import com.sirius.DevMate.domain.join.Application;
import com.sirius.DevMate.domain.join.Membership;
import com.sirius.DevMate.domain.project.Project;
import com.sirius.DevMate.domain.user.User;
import com.sirius.DevMate.exception.UserNotFound;
import com.sirius.DevMate.exception.ProjectException;
import com.sirius.DevMate.repository.user.NotificationRepository;
import com.sirius.DevMate.repository.join.ApplicationRepository;
import com.sirius.DevMate.repository.join.MembershipRepository;
import com.sirius.DevMate.repository.project.ProjectRepository;
import com.sirius.DevMate.service.user.NotificationService;
import com.sirius.DevMate.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    private static final Integer LIST_SIZE = 10;

    public void newProject(NewProjectDto newProjectDto) throws UserNotFound {

        Project newProject = Project
                .builder()
                .title(newProjectDto.title())
                .shortDescription(newProjectDto.shortDescription())
                .recruitSize(newProjectDto.recruitSize())
                .currentSize(newProjectDto.currentSize())
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

        Membership newMembership = Membership
                .builder()
                .user(userService.getUser())
                .project(newProject)
                .membershipRole(MembershipRole.LEADER)
                .membershipStatus(MembershipStatus.PARTICIPATION)
                .build();

        projectRepository.save(newProject);
        membershipRepository.save(newMembership);
        newProject.getMemberships().add(newMembership);

    }

    public Optional<Project> getProjectById(Long id) {
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

    public void newApplication(Long projectId, Position position, String content) throws ProjectException, UserNotFound {
        Optional<Project> project = projectRepository.findById(projectId);
        User loginUser = userService.getUser();
        if (project.isEmpty()) {
            throw new ProjectException("존재 하지 않는 프로젝트 입니다");
        } else {

            if (membershipRepository.existByUserId(loginUser.getUserId())) {
                throw new ProjectException("두번 이상 지원은 불가합니다");
            }

            Application newApplication = Application.builder()
                    .user(loginUser)
                    .project(project.get())
                    .applyPosition(position)
                    .content(content)
                    .applicationStatus(ApplicationStatus.STANDBY)
                    .build();

            applicationRepository.save(newApplication);
            project.get().getApplications().add(newApplication);
            notificationService.notifyApplicationSubmittedNotifications(loginUser, project.get(), newApplication);

        }
    }
}
