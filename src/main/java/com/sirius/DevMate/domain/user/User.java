package com.sirius.DevMate.domain.user;

import com.sirius.DevMate.config.security.oauth.OAuth2Attributes;
import com.sirius.DevMate.domain.common.*;
import com.sirius.DevMate.domain.common.project.*;
import com.sirius.DevMate.domain.common.sys.OAuth2Provider;
import com.sirius.DevMate.domain.common.user.PreferredAtmosphere;
import com.sirius.DevMate.domain.common.user.PreferredDuration;
import com.sirius.DevMate.domain.common.user.Regions;
import com.sirius.DevMate.domain.common.user.SkillLevel;
import com.sirius.DevMate.domain.join.Membership;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_user", columnNames = {"email"}),
        @UniqueConstraint(name = "uk_user_provider", columnNames = {"provider","provider_id"})
}
)
@Getter
@NoArgsConstructor @AllArgsConstructor @Builder(toBuilder = true)
public class User extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(length = 100, unique = true) // google/github 이메일
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 120)
    private OAuth2Provider provider;

    @Column(nullable = false, length = 80)
    private String providerId; // google sub/ github id

    @Column(length=300)
    private String avatarUrl;

    @Column(nullable=false, length=20)
    private String role;

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

    @PrePersist void prePersist() {
        createdAt = Instant.from(LocalDateTime.now());
        updatedAt = createdAt;
    }
    @PreUpdate void preUpdate() { updatedAt = Instant.from(LocalDateTime.now()); }

    /** 최초 가입용 정적 팩토리: Builder를 감싸 가독성↑ */
    public static  User fromOAuth(OAuth2Attributes attr, String resolvedEmail) {
        return User.builder()
                .provider(attr.provider())
                .providerId(attr.providerId())
                .email(resolvedEmail)      // GitHub는 null일 수 있음
                .name(attr.name())
                .avatarUrl(attr.avatarUrl())
                .role("ROLE_USER")
                .build();
    }
    /** 재로그인 시 프로필 동기화 */
    public void syncFromOAuth(OAuth2Attributes attr, String resolvedEmail) {
        // provider/providerId는 동일 계정 식별 키이므로 변경하지 않음
        if (resolvedEmail != null) this.email = resolvedEmail;
        this.name = attr.name();
        this.avatarUrl = attr.avatarUrl();
        // role 정책이 바뀌면 여기에서 관리
    }



}
