package com.sirius.DevMate.domain.user;

import com.sirius.DevMate.domain.common.user.StackType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stack", uniqueConstraints = @UniqueConstraint(name = "uk_stack",
    columnNames = {"user_id", "stack_name", "stack_type"}
))
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class Stack {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long stackId;

    @Column(nullable = false, length = 20)
    private String stackName;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StackType stackType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
