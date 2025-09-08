package com.sirius.DevMate.service.join;

import com.sirius.DevMate.domain.common.project.MembershipRole;
import com.sirius.DevMate.domain.common.project.MembershipStatus;
import com.sirius.DevMate.domain.join.Membership;
import com.sirius.DevMate.domain.project.Project;
import com.sirius.DevMate.domain.user.User;
import com.sirius.DevMate.repository.join.MembershipRepository;
import com.sirius.DevMate.service.project.ProjectService;
import com.sirius.DevMate.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final UserService userService;
    private final ProjectService projectService;

    public Membership createMembership(
            User user,
            Project project,
            MembershipRole membershipRole,
            MembershipStatus membershipStatus) {
        Membership newMembership = Membership
                .builder()
                .user(user)
                .project(project)
                .membershipRole(membershipRole)
                .membershipStatus(membershipStatus)
                .build();

        membershipRepository.save(newMembership);
        project.getMemberships().add(newMembership);
        user.getMyMemberships().add(newMembership);

        return newMembership;
    }

    public List<Membership> getLeaderMembership(User loginUser) {
        List<Membership> leaderMemberships = new ArrayList<>();
        for (Membership membership : loginUser.getMyMemberships()) {
            if (membership.getMembershipRole() == MembershipRole.LEADER) {
                leaderMemberships.add(membership);
            }
        }
        return leaderMemberships;
    }

    public Membership getApplicantMembership(Long userId, Long projectId) {
        return membershipRepository.findByUserId(userId, projectId);
    }

    public void updateProjectMembershipStatus(Long projectId) {
        Project project = projectService.getProjectById(projectId);
        List<Membership> memberships = project.getMemberships();
        for (Membership membership : memberships) {
            membership.changeMembershipStatus(MembershipStatus.PARTICIPATION);
        }
    }
}
