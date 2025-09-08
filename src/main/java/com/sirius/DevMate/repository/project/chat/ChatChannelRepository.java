package com.sirius.DevMate.repository.project.chat;

import com.sirius.DevMate.domain.project.chat.ChatChannel;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatChannelRepository {

    private final EntityManager em;

    public void save(ChatChannel chatChannel) {
        if (chatChannel.getChatChannelId() == null) {
            em.persist(chatChannel);
        } else {
            em.merge(chatChannel);
        }
    }

    public void delete(ChatChannel chatChannel) {
        em.remove(chatChannel);
    }
}
