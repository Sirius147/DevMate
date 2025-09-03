package com.sirius.DevMate.domain.join;

import com.sirius.DevMate.domain.common.BaseTimeEntity;
import com.sirius.DevMate.domain.common.project.MembershipRole;
import com.sirius.DevMate.domain.common.project.MembershipStatus;
import com.sirius.DevMate.domain.project.Project;
import com.sirius.DevMate.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "membership",
        uniqueConstraints = @UniqueConstraint(name = "uk_membership",
                columnNames = {"project_id", "user_id"}))
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class Membership extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long membershipId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MembershipRole membershipRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private MembershipStatus membershipStatus;

    public void changeMembershipStatus(MembershipStatus membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

}
