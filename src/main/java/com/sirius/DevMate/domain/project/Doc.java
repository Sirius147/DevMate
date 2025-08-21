package com.sirius.DevMate.domain.project;

import com.sirius.DevMate.domain.common.BaseTimeEntity;
import com.sirius.DevMate.domain.common.project.RequestMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

}
