package com.sirius.DevMate.domain.project.chat;

import com.sirius.DevMate.domain.common.BaseTimeEntity;
import com.sirius.DevMate.domain.project.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_channel", uniqueConstraints = @UniqueConstraint(name = "uk_chat_channel",
columnNames = {"project_id"}))
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatChannel extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long chatChannelId;

    @OneToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(length = 30)
    private String name;

    @Column(nullable = false)
    private Integer size;

    @OneToMany(mappedBy = "chatChannel", fetch = FetchType.LAZY) @Builder.Default
    private List<ChatMembership> chatMemberships = new ArrayList<>();

    @OneToMany(mappedBy = "chatChannel", fetch = FetchType.LAZY) @Builder.Default
    private List<ChatMessage> chatMessages = new ArrayList<>();

}
