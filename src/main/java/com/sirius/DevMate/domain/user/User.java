package com.sirius.DevMate.domain.user;

import com.sirius.DevMate.domain.common.BaseTimeEntity;
import com.sirius.DevMate.domain.common.project.CollaborateStyle;
import com.sirius.DevMate.domain.common.project.Position;
import com.sirius.DevMate.domain.common.sys.OAuth2Provider;
import com.sirius.DevMate.domain.common.user.PreferredAtmosphere;
import com.sirius.DevMate.domain.common.user.Regions;
import com.sirius.DevMate.domain.common.user.SkillLevel;
import com.sirius.DevMate.domain.join.Membership;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_user", columnNames = {"email"}),
        @UniqueConstraint(name = "uk_user_provider", columnNames = {"provider","provider_id"}),
        @UniqueConstraint(name = "uk_user_nickname", columnNames = {"nickname"})
}
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder(toBuilder = true)
public class User extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(length = 30)
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

    @Column(length = 30, unique = true)
    private String nickname;

    @Column(length = 100)
    private String intro;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Regions region;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SkillLevel skillLevel;

    private Integer popularity; // 0~10

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PreferredAtmosphere preferredAtmosphere;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CollaborateStyle preferredCollaborateStyle;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Position position; // 디자인/백엔드/프론트/기획

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true) @Builder.Default
    private List<Stack> stacks = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true) @Builder.Default
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true) @Builder.Default
    private List<Membership> myMemberships = new ArrayList<>();

    @PrePersist void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        updatedAt = createdAt;
    }
    @PreUpdate void preUpdate() { updatedAt = Instant.now(); }

    /** 최초 가입용 정적 팩토리: Builder를 감싸 가독성↑ */
    public static  User fromOAuth(OAuth2Provider provider,String providerId,
            String email, String name, String avatarUrl) {
        String randomNickname = name + provider + "-" + UUID.randomUUID().toString().substring(0, 8);

        return User.builder()
                .provider(provider)
                .providerId(providerId)
                .email(email)      // GitHub는 null일 수 있음
                .name(name)
                .nickname(randomNickname)
                .avatarUrl(avatarUrl)
                .role("USER")
                .build();
    }
    /** 재로그인 시 프로필 동기화 */
    public void syncFromOAuth(String name, String avatarUrl, String email) {
        // provider/providerId는 동일 계정 식별 키이므로 변경하지 않음
        if (email != null) this.email = email;
        this.name = name;
        this.avatarUrl = avatarUrl;
        // role 정책이 바뀌면 여기에서 관리
    }



}
