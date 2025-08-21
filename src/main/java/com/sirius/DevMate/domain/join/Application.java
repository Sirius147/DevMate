package com.sirius.DevMate.domain.join;

import com.sirius.DevMate.domain.common.BaseTimeEntity;
import com.sirius.DevMate.domain.common.project.ApplicationStatus;
import com.sirius.DevMate.domain.project.Project;
import com.sirius.DevMate.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "application",
        uniqueConstraints = @UniqueConstraint(name = "uk_application",
                columnNames = {"project_id", "user_id"}))
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ApplicationStatus applicationStatus;
}
