package com.sirius.DevMate.domain.project;

import com.sirius.DevMate.domain.common.BaseTimeEntity;
import com.sirius.DevMate.domain.common.project.RequestMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "doc")
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class Doc extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long docId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(length = 30)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RequestMethod method;

    @Lob
    @Column(nullable = false)
    private String path;

    @Lob
    private String responseExample;

    @Lob
    private String parameter;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public void changeName(String name) {
        this.name = name;
    }
    public void changePath(String path) {
        this.path = path;
    }
    public void changeMethod(RequestMethod method) {
        this.method = method;
    }
    public void changeResponseExample(String responseExample) {
        this.responseExample = responseExample;
    }
    public void changeParameter(String parameter) {
        this.parameter = parameter;
    }




}
