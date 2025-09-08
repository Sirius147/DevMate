package com.sirius.DevMate.service.project.chat;

import com.sirius.DevMate.config.s3.service.S3Service;
import com.sirius.DevMate.controller.dto.chat.ChatMessageDto;
import com.sirius.DevMate.controller.dto.chat.SendMessageRequest;
import com.sirius.DevMate.domain.common.sys.Direction;
import com.sirius.DevMate.domain.common.sys.MessageType;
import com.sirius.DevMate.domain.project.chat.ChatAttachment;
import com.sirius.DevMate.domain.project.chat.ChatChannel;
import com.sirius.DevMate.domain.project.chat.ChatMembership;
import com.sirius.DevMate.domain.project.chat.ChatMessage;
import com.sirius.DevMate.repository.project.ProjectRepository;
import com.sirius.DevMate.repository.project.chat.ChatAttachmentRepository;
import com.sirius.DevMate.repository.project.chat.ChatMembershipRepository;
import com.sirius.DevMate.repository.project.chat.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatMembershipRepository chatMembershipRepository;
    private final ChatAttachmentRepository chatAttachmentRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ProjectRepository projectRepository;
    private final S3Service s3Service;

    // 메시지 전송(텍스트/첨부)
    public ChatMessageDto sendMessage(Long channelId, SendMessageRequest sendMessageRequest) {
        // 1) 멤버십 검증 (그룹 소속 보장)
        ChatMembership membership = chatMembershipRepository.findByChannelAndUser(channelId, sendMessageRequest.userId());
        if (membership == null) {
            throw new IllegalStateException("채널에 가입되지 않은 사용자입니다.");
        }

        // 2) 메시지 생성 (수정 불가 정책: 엔티티에 수정용 세터/메서드 두지 않기)
        ChatChannel channel = projectRepository.findChatChannelByChatChannelId(channelId);

        ChatMessage msg = ChatMessage.builder()
                .chatChannel(channel)
                .chatMembership(membership)
                .messageType(MessageType.TEXT) // 파일만 전송하면 FILE/IMAGE 등으로 설정 가능
                .content(sendMessageRequest.content())
                .build();

        chatMessageRepository.save(msg);

        // 3) 첨부 저장 (있으면)
        if (sendMessageRequest.attachments() != null && !sendMessageRequest.attachments().isEmpty()) {

            for (SendMessageRequest.AttachmentPayload a : sendMessageRequest.attachments()) {
                // 1) HEAD로 실제 업로드/메타 확인
                HeadObjectResponse head = s3Service.head(a.storageKey());
                long actualSize = head.contentLength();
                String actualType = head.contentType();

                // 2) 클라이언트가 보낸 메타와 교차검증 (기본 체크)
                if (!Objects.equals(a.contentType(), actualType)) {
                    // 약간의 차이가 날 수 있어도, whitelist 기준으로 최종 actualType만 저장해도 됨
                    // 여기선 보수적으로 막음
                    throw new IllegalArgumentException("업로드된 파일 형식이 일치하지 않습니다.");
                }
                if (!a.fileSize().equals(actualSize)) {
                    throw new IllegalArgumentException("업로드된 파일 크기가 일치하지 않습니다.");
                }
            }

            List<ChatAttachment> list = sendMessageRequest.attachments().stream().map(a ->
                    ChatAttachment.builder()
                            .chatMessage(msg)
                            .storageKey(a.storageKey())
                            .url(a.url())
                            .filename(a.filename())
                            .contentType(a.contentType())
                            .fileSize(a.fileSize())
                            .build()
            ).toList();
            chatAttachmentRepository.saveAll(list);

            msg.getChatAttachments().addAll(list);
        }

        // 4) DTO로 변환
        return toDto(msg);
    }

    private ChatMessageDto toDto(ChatMessage m) {
        List<ChatMessageDto.AttachmentDto> atts = Optional.ofNullable(m.getChatAttachments())
                .orElseGet(List::of)
                .stream()
                .map(a -> new ChatMessageDto.AttachmentDto(
                        a.getChatAttachmentId(),
                        a.getUrl(),
                        a.getFilename(),
                        a.getContentType(),
                        a.getFileSize()
                )).toList();

        return new ChatMessageDto(
                m.getChatMessageId(),
                m.getChatChannel().getChatChannelId(),
                m.getChatMembership().getUser().getUserId(),
                m.getMessageType().name(),
                m.getContent(),
                m.getCreatedAt(),
                atts
        );
    }

    public List<ChatMessageDto> listMessages(Long channelId, Long anchorId, Direction dir, int limit) {
        List<ChatMessage> list = (dir == Direction.NEWER)
                ? chatMessageRepository.findNewer(channelId, anchorId, limit)
                : chatMessageRepository.findOlder(channelId, anchorId, limit);
        // older는 내림차순으로 내려오므로, UI가 오래된→최신 순으로 원하면 역순 처리
        if (dir == Direction.OLDER) {
            Collections.reverse(list); // 결과를 오래된→최신 순으로 맞춤(선호에 따라)
        }
        return list.stream().map(this::toDto).toList();
    }

    // 읽음 처리: 특정 messageId까지 읽음
    public void markRead(Long channelId, Long userId, Long messageId) {
        ChatMembership membership = chatMembershipRepository.findByChannelAndUser(channelId, userId);
        if (membership == null) throw new IllegalStateException("채널에 가입되지 않은 사용자입니다.");
        chatMembershipRepository.markReadUpTo(membership.getChatMembershipId(), messageId);
    }

    public long unreadCount(Long channelId, Long userId) {
        ChatMembership membership = chatMembershipRepository.findByChannelAndUser(channelId, userId);
        if (membership == null) throw new IllegalStateException("채널에 가입되지 않은 사용자입니다.");
        Long last = membership.getLastReadMessageId();
        return chatMessageRepository.countUnread(channelId, last);
    }

}
