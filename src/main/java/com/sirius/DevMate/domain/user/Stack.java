package com.sirius.DevMate.domain.user;

import com.sirius.DevMate.domain.common.StackType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stack")
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class Stack {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String stackId;

    @Column(nullable = false, length = 20)
    private String stackName;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StackType stackType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
