package com.sirius.DevMate.domain.user;

import com.sirius.DevMate.domain.common.*;
import com.sirius.DevMate.domain.common.project.*;
import com.sirius.DevMate.domain.common.sys.OAuthProvider;
import com.sirius.DevMate.domain.common.user.PreferredAtmosphere;
import com.sirius.DevMate.domain.common.user.PreferredDuration;
import com.sirius.DevMate.domain.common.user.Regions;
import com.sirius.DevMate.domain.common.user.SkillLevel;
import com.sirius.DevMate.domain.join.Membership;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "uk_user",
columnNames = {"email"}))
@Getter @NoArgsConstructor @AllArgsConstructor @Builder(toBuilder = true)
public class User extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(length = 100, unique = true) // google/github 이메일
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 120)
    private OAuthProvider provider;

    @Column(nullable = false, length = 80)
    private String providerId; // google sub/ github id

    @Column(length=300)
    private String pictureUrl;

    @Column(nullable=false, length=20)
    @Builder.Default
    private String role = "ROLE_USER";

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

    private Integer popularity; // 0~10

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PreferredAtmosphere preferredAtmosphere;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PreferredDuration preferredDuration;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CollaborateStyle preferredCollaborateStyle;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Position position; // 디자인/백엔드/프론트/기획

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) @Builder.Default
    private List<Stack> stacks = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) @Builder.Default
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY) @Builder.Default
    private List<Membership> myMemberships = new ArrayList<>();

    /*
     * (선택) 도메인 메서드 예시 — setter 대신 의미 있는 변경만 허용하고 싶다면 사용
     * public User updateProfile(String name, String pictureUrl) {
     *     return this.toBuilder()
     *               .name(name)
     *               .pictureUrl(pictureUrl)
     *               .build();
     * }
     * public User connectProvider(String provider, String providerId) {
     *     return this.toBuilder()
     *               .provider(provider)
     *               .providerId(providerId)
     *               .build();
     * }
     */


}
