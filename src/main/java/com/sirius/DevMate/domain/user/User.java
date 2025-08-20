package com.sirius.DevMate.domain.user;

import com.sirius.DevMate.domain.common.*;
import com.sirius.DevMate.domain.common.project.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class User extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(length = 100)
    private String email;

    @Column(nullable = false, length = 30, unique = true)
    private String nickname;

    @Column(length = 100)
    private String intro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Regions region;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SkillLevel skillLevel;

    @Column(nullable = false)
    private Integer popularity; // 0~10

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PreferredAtmosphere preferredAtmosphere;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PreferredTeamSize preferredTeamSize;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PreferredDuration preferredDuration;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CollaborateStyle preferredWorkStyle;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PreferredRole preferredRole; // 디자인/백엔드/프론트/기획

    @OneToMany(mappedBy = "user")
    private List<Stack> stacks = new ArrayList<>();
}
