package com.sirius.DevMate.domain.join;

import com.sirius.DevMate.domain.common.BaseTimeEntity;
import com.sirius.DevMate.domain.common.project.ApplicationStatus;
import com.sirius.DevMate.domain.common.project.Position;
import com.sirius.DevMate.domain.project.Project;
import com.sirius.DevMate.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;


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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Position applyPosition;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ApplicationStatus applicationStatus;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public void changeApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
}
