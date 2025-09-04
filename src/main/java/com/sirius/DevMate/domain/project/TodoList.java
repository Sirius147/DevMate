package com.sirius.DevMate.domain.project;

import com.sirius.DevMate.domain.common.BaseTimeEntity;
import com.sirius.DevMate.domain.common.project.Position;
import com.sirius.DevMate.domain.common.project.Priority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "to_do_list")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoList extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long toDoListId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(length = 30)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Position position;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Priority priority;

    @Temporal(TemporalType.DATE)
    private LocalDate startDate;

    @Temporal(TemporalType.DATE)
    private LocalDate endDate;

    @Column(nullable = false)
    private boolean done;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
        this.done = false;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public void changeTitle(Object title) {
        this.title = (String) title;
    }

    public void changePosition(Position position) {
        this.position = position;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changePriority(Priority priority) {
        this.priority = priority;
    }

    public void changeStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void changeEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void changeDone(Boolean done) {
        this.done = done;
    }
}
