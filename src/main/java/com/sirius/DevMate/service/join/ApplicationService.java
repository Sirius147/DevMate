package com.sirius.DevMate.service.join;


import com.sirius.DevMate.controller.dto.response.ApplicationResponseDto;
import com.sirius.DevMate.controller.dto.response.PageList;
import com.sirius.DevMate.controller.dto.response.RecruitingProjectApplicationsDto;
import com.sirius.DevMate.domain.common.project.*;
import com.sirius.DevMate.domain.join.Application;
import com.sirius.DevMate.domain.join.Membership;
import com.sirius.DevMate.domain.project.Project;
import com.sirius.DevMate.domain.user.User;
import com.sirius.DevMate.exception.ProjectException;
import com.sirius.DevMate.exception.UserNotFound;
import com.sirius.DevMate.repository.join.ApplicationRepository;
import com.sirius.DevMate.repository.join.MembershipRepository;
import com.sirius.DevMate.repository.project.ProjectRepository;
import com.sirius.DevMate.service.project.ProjectService;
import com.sirius.DevMate.service.user.NotificationService;
import com.sirius.DevMate.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {

    private final UserService userService;
    private final ProjectService projectService;
    private final NotificationService notificationService;
    private final MembershipService membershipService;
    private final ProjectRepository projectRepository;
    private final MembershipRepository membershipRepository;
    private final ApplicationRepository applicationRepository;


    public void newApplication(Long projectId, Position position, String content) throws ProjectException, UserNotFound {
        Project project = projectRepository.findById(projectId);
        User loginUser = userService.getUser();

            if (membershipRepository.existByUserId(loginUser.getUserId(), project.getProjectId())) {
                throw new ProjectException("두번 이상 지원은 불가합니다");
            }

        switch (position) {
            case Position.BACKEND -> {
                if (project.getBackendMembers() - project.getCurrentBackend() <= 0) {
                    throw new ProjectException("backend 멤버가 다 차서 지원 불가능 합니다");
                }
            }
            case Position.FRONTEND -> {
                if (project.getFrontendMembers() - project.getCurrentFrontend() <= 0) {
                    throw new ProjectException("frontend 멤버가 다 차서 지원 불가능 합니다");
                }
            }
            case Position.DESIGN -> {
                if (project.getDesignMembers() - project.getCurrentDesign() <= 0) {
                    throw new ProjectException("design 멤버가 다 차서 지원 불가능 합니다");
                }
            }
            case Position.PM -> {
                if (project.getPmMembers() - project.getCurrentPm() <= 0) {
                    throw new ProjectException("pm 멤버가 다 차서 지원 불가능 합니다");
                }
            }
        }

            Application newApplication = Application.builder()
                    .user(loginUser)
                    .project(project)
                    .applyPosition(position)
                    .content(content)
                    .applicationStatus(ApplicationStatus.STANDBY)
                    .build();

            applicationRepository.save(newApplication);
            project.getApplications().add(newApplication);
            loginUser.getMyApplications().add(newApplication);
            notificationService.notifyApplicationSubmittedNotifications(loginUser, project, newApplication);


    }

    public Application getApplicationByApplicationId(Long applicationId) {
        return applicationRepository.findById(applicationId);
    }

    public PageList<RecruitingProjectApplicationsDto> getApplicationsByProjectId(Long projectId) throws ProjectException {
        Project projectById = projectService.getProjectById(projectId);
        if (projectById.getApplications().isEmpty()) {
                return new PageList<>(null, 0L);
            }
            List<RecruitingProjectApplicationsDto> recruitingProjectApplicationsDtos = new ArrayList<>();
            for (Application application : projectById.getApplications()) {
                recruitingProjectApplicationsDtos.add(
                        new RecruitingProjectApplicationsDto(
                                application.getUser().getNickname(),
                                application.getApplyPosition(),
                                application.getContent(),
                                application.getApplicationStatus()
                        )
                );
            }
            return new PageList<>(recruitingProjectApplicationsDtos, (long) projectById.getApplications().size());

    }

    public void processApplication(Long projectId, Long applicationId, Boolean accept) throws ProjectException, UserNotFound {
        // 지원 수락
        if (accept) {
            // application status 변경, project status , 인원 관리 변경, 지원자 membership status 변경,
            // 알림 보내기 리더와 지원자 모두 + 추가로 project Status가 변하면 보내는 알림 추가하기

            Application application = applicationRepository.findById(applicationId);
            Project project = projectRepository.findById(projectId);
            // 지원서 상태 변경
            application.changeApplicationStatus(ApplicationStatus.APPROVAL);
            // 유저 멤버십 생성
            Membership applicantMembership = membershipService.createMembership(application.getUser(),
                    project,
                    MembershipRole.MEMBER,
                    MembershipStatus.WAITING
            );

            // 프로젝트 인원 변경
            switch (application.getApplyPosition()) {
                case Position.BACKEND -> {
                    project.changeCurrentBackend();
                }
                case Position.FRONTEND -> {
                    project.changeCurrentFrontend();
                }
                case Position.DESIGN -> {
                    project.changeCurrentDesign();
                }
                case Position.PM -> {
                    project.changeCurrentPm();
                }

            }
            // 프로젝트가 진행 가능 할 시에
            if (project.getCurrentSize().equals(project.getRecruitSize())) {
//                project.changeProjectStatus(ProjectStatus.IN_PROGRESS);
                projectService.endRecruitingStartProject(projectId);
                notificationService.notifyProjectStartNotification(project.getMemberships());
            }
            // 그렇지 않은 경우
            else {
                // 승인 알림 리더, 멤버에게
                notificationService.notifyApplicationAcceptedNotification(
                        userService.getUser(), applicantMembership.getUser(), project);
            }


        }
        // 지원 거부
        else {
            Application application = getApplicationByApplicationId(applicationId);
            application.changeApplicationStatus(ApplicationStatus.REJECT);

            notificationService.notifyApplicationRejectedNotification(application);
        }
    }

    public PageList<ApplicationResponseDto> getApplicationsByUserId() throws UserNotFound {
        List<Application> applications = applicationRepository.findByUserId(userService.getUser().getUserId());
        List<ApplicationResponseDto> applicationResponseDtos = new ArrayList<>();
        for (Application application : applications) {
            applicationResponseDtos.add(
                    new ApplicationResponseDto(
                            application.getApplicationId(),
                            application.getProject().getTitle(),
                            application.getApplicationStatus(),
                            application.getContent()
                    )
            );
        }
        return new PageList<>(applicationResponseDtos, (long) applicationResponseDtos.size());
    }

    public void deleteApplicationByApplicationId(Long applicationId) throws UserNotFound {
        Application application = applicationRepository.findById(applicationId);
        application.getUser().getMyApplications().remove(application);
        application.getProject().getApplications().remove(application);
        applicationRepository.delete(application);
    }
}
