package com.sirius.DevMate.repository.project.chat;

import com.sirius.DevMate.domain.project.chat.ChatAttachment;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatAttachmentRepository {
    private final EntityManager em;

    public void save(ChatAttachment chatAttachment) {
        em.persist(chatAttachment);
    }

    public void saveAll(List<ChatAttachment> chatAttachments) {
        chatAttachments.forEach(em::persist);
    }
}
