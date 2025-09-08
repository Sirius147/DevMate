package com.sirius.DevMate.repository.project.chat;

import com.sirius.DevMate.domain.project.chat.ChatMessage;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {

    private final EntityManager em;

    public void save(ChatMessage chatMessage) {
        em.persist(chatMessage);
    }

    // 채널 최신 메시지 id
    public Long findLatestIdInChannel(Long channelId) {
        return em.createQuery(
                        "select max(m.chatMessageId) from ChatMessage m where m.chatChannel.chatChannelId = :channelId",
                        Long.class
                ).setParameter("channelId", channelId)
                .getSingleResult();
    }

    // older: anchorId보다 작은 것들 (내림차순)
    public List<ChatMessage> findOlder(Long channelId, Long anchorId, int limit) {
        String jpql = """
            select m from ChatMessage m
            join fetch m.chatMembership cm
            join fetch cm.user u
            left join fetch m.chatAttachments a
            where m.chatChannel.chatChannelId = :channelId
              and (:anchorId is null or m.chatMessageId < :anchorId)
            order by m.chatMessageId desc
        """;
        return em.createQuery(jpql, ChatMessage.class)
                .setParameter("channelId", channelId)
                .setParameter("anchorId", anchorId)
                .setMaxResults(limit)
                .getResultList();
    }

    // newer: anchorId보다 큰 것들 (오름차순)
    public List<ChatMessage> findNewer(Long channelId, Long anchorId, int limit) {
        String jpql = """
            select m from ChatMessage m
            join fetch m.chatMembership cm
            join fetch cm.user u
            left join fetch m.chatAttachments a
            where m.chatChannel.chatChannelId = :channelId
              and (:anchorId is not null and m.chatMessageId > :anchorId)
            order by m.chatMessageId asc
        """;
        return em.createQuery(jpql, ChatMessage.class)
                .setParameter("channelId", channelId)
                .setParameter("anchorId", anchorId)
                .setMaxResults(limit)
                .getResultList();
    }

    // 미읽음 개수 = lastReadId 이후
    public long countUnread(Long channelId, Long lastReadMessageId) {
        String base = "select count(m) from ChatMessage m where m.chatChannel.chatChannelId = :channelId";
        if (lastReadMessageId == null || lastReadMessageId == 0L) {
            return em.createQuery(base, Long.class)
                    .setParameter("channelId", channelId)
                    .getSingleResult();
        }
        String jpql = base + " and m.chatMessageId > :lastReadMessageId";
        return em.createQuery(jpql, Long.class)
                .setParameter("channelId", channelId)
                .setParameter("lastReadMessageId", lastReadMessageId)
                .getSingleResult();
    }
}
