package com.sirius.DevMate.domain.join;

import com.sirius.DevMate.domain.project.Project;
import com.sirius.DevMate.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review",
        uniqueConstraints = @UniqueConstraint(name = "uk_review",
                columnNames = {"project_id", "user_id"}))
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Lob
    private String content;

    @Column(nullable = false)
    private Integer star;
}
