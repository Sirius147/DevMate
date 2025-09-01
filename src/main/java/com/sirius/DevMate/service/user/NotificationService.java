package com.sirius.DevMate.service.user;

import com.sirius.DevMate.domain.common.project.MembershipRole;
import com.sirius.DevMate.domain.common.sys.NotificationType;
import com.sirius.DevMate.domain.join.Application;
import com.sirius.DevMate.domain.join.Membership;
import com.sirius.DevMate.domain.project.Project;
import com.sirius.DevMate.domain.user.Notification;
import com.sirius.DevMate.domain.user.User;
import com.sirius.DevMate.exception.ProjectException;
import com.sirius.DevMate.repository.user.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private Notification createNotification(
            User user,
            NotificationType notificationType,
            String content
    ) {
        return Notification.builder()
                .user(user)
                .notificationType(notificationType)
                .content(content)
                .build();
    }

    public void notifyApplicationSubmittedNotifications(User loginUser, Project project, Application application) throws ProjectException {

        String applicantContent = "프로젝트" + project.getTitle() + "에 지원 완료!";
        String leaderContent = "프로젝트" + project.getTitle() + "에 지원자: " +
                loginUser.getNickname() + "가 포지션: " + application.getApplyPosition() +
                "으로 지원하였습니다!";

        User projectLeader = project.getMemberships().
                stream()
                .filter(membership -> membership.getMembershipRole().equals(MembershipRole.LEADER))
                .map(Membership::getUser)
                .findFirst()
                .orElseThrow(() -> new ProjectException("leader Not Found"));

        Notification applicantNotification = createNotification(
                loginUser,
                NotificationType.APPLICATION_SUBMITTED,
                applicantContent);

        Notification leaderNotification = createNotification(
                projectLeader,
                NotificationType.APPLICATION_SUBMITTED,
                leaderContent
        );


        notificationRepository.save(applicantNotification);
        loginUser.getNotifications().add(applicantNotification);
        notificationRepository.save(leaderNotification);
        projectLeader.getNotifications().add(leaderNotification);
    }

    public void notifyApplicationAcceptedNotification(User leaderUser, User applicantUser, Project project, Application application) throws ProjectException {

        String applicantContent = "프로젝트" + project.getTitle() + "에 참여 승인!";
        String leaderContent = "프로젝트" + project.getTitle() + "에 지원자: " +
                applicantUser.getNickname() + "가 포지션: " + application.getApplyPosition() +
                "으로 배정되었습니다!";
    }
}
