package com.sirius.DevMate.domain.project;

import com.sirius.DevMate.domain.common.BaseTimeEntity;
import com.sirius.DevMate.domain.common.project.CollaborateStyle;
import com.sirius.DevMate.domain.common.user.Regions;
import com.sirius.DevMate.domain.common.user.SkillLevel;
import com.sirius.DevMate.domain.join.Membership;
import com.sirius.DevMate.domain.join.Review;
import com.sirius.DevMate.domain.project.chat.ChatChannel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project", indexes = {
        @Index(name = "idx_project_title", columnList = "title"),
        @Index(name = "idx_project_region", columnList = "preferred_region"),
        @Index(name = "idx_project_level", columnList = "project_level"),
        @Index(name = "idx_project_collaborate_style", columnList = "collaborate_style"),
        @Index(name = "idx_project_dates", columnList = "start_date, end_date")
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectId;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 300)
    private String shortDescription; // 간단 설명

    @Column(nullable = false)
    private Integer recruitSize; // 모집인원

    @Column(nullable = false)
    private Integer currentSize;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CollaborateStyle collaborateStyle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Regions preferredRegion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SkillLevel projectLevel;

    @Column(nullable = false)
    private Integer backendMembers;

    @Column(nullable = false)
    private Integer currentBackend;

    @Column(nullable = false)
    private Integer frontendMembers;

    @Column(nullable = false)
    private Integer currentFrontend;

    @Column(nullable = false)
    private Integer designMembers;

    @Column(nullable = false)
    private Integer currentDesign;

    @Column(nullable = false)
    private Integer pmMembers;

    @Column(nullable = false)
    private Integer currentPm;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY) @Builder.Default
    private List<Membership> memberships = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY) @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY) @Builder.Default
    private List<TodoList> todoLists = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY) @Builder.Default
    private List<Doc> docs = new ArrayList<>();

    @OneToOne(mappedBy = "project")
    private ChatChannel chatChannel;

}
