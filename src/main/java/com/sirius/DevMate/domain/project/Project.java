package com.sirius.DevMate.domain.project;

import com.sirius.DevMate.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project")
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
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

}
