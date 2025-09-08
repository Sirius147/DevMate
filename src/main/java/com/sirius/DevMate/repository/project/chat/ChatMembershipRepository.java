package com.sirius.DevMate.repository.project.chat;

import com.sirius.DevMate.domain.project.chat.ChatMembership;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMembershipRepository {

    private final EntityManager em;

    public void save(ChatMembership chatMembership) {
        if (chatMembership.getChatMembershipId() == null) {
            em.persist(chatMembership);
        } else {
            em.merge(chatMembership);
        }
    }

    public ChatMembership findByChannelAndUser(Long channelId, Long userId) {
        List<ChatMembership> list = em.createQuery("""
            select cm from ChatMembership cm
            join fetch cm.chatChannel ch
            join fetch cm.user u
            where ch.chatChannelId = :ch and u.userId = :uid
        """, ChatMembership.class)
                .setParameter("ch", channelId)
                .setParameter("uid", userId)
                .getResultList();

        if (list.isEmpty()) return null;
        return list.getFirst();
    }

    @Transactional
    public void markReadUpTo(Long chatMembershipId, Long messageId) {
        em.createQuery("""
            update ChatMembership cm
               set cm.lastReadChatMessageId =
                   case when cm.lastReadChatMessageId is null then :mid
                        when cm.lastReadChatMessageId < :mid then :mid
                        else cm.lastReadChatMessageId end
             where cm.chatMembershipId = :cmid
        """)
                .setParameter("mid", messageId)
                .setParameter("cmid", chatMembershipId)
                .executeUpdate();
    }
}
