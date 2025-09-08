package com.sirius.DevMate.service.user;

import com.sirius.DevMate.domain.common.project.MembershipRole;
import com.sirius.DevMate.domain.common.project.MembershipStatus;
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

import java.util.List;

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
        Notification notification = Notification.builder()
                .user(user)
                .notificationType(notificationType)
                .content(content)
                .build();

        notificationRepository.save(notification);
        user.getNotifications().add(notification);

        return notification;
    }

//    public void expireNotification(Notification notification) throws UserNotFound {
//        User user = userService.getUser();
//        user.getNotifications().remove(notification);
//        notificationRepository.delete(notification);
//    }

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

    }

    public void notifyApplicationAcceptedNotification(User leaderUser, User applicantUser, Project project) throws ProjectException {

        createNotification(leaderUser, NotificationType.APPLICATION_ACCEPTED,
                "프로젝트" + project.getTitle() + " 에 지원자 " + applicantUser.getNickname() +
                        " 승인 완료");

        createNotification(applicantUser, NotificationType.APPLICATION_ACCEPTED,
                "프로젝트" + project.getTitle() + " 에 참가 승인 되었습니다");
    }

    public void notifyProjectStartNotification(List<Membership> memberships) {
        for (Membership membership : memberships) {
            membership.changeMembershipStatus(MembershipStatus.PARTICIPATION);
            createNotification(membership.getUser(), NotificationType.PROJECT_START,
                    "프로젝트" + membership.getProject().getTitle() + "(이)가 시작하였습니다! " +
                            "내 프로젝트 창에서 확인하세요");
        }
    }

    public void notifyApplicationRejectedNotification(Application application) {
        createNotification(application.getUser(), NotificationType.APPLICATION_REJECTED,
                "지원하신 프로젝트 " + application.getProject().getTitle() + " 에 참여 거부 되었습니다");
    }
}
