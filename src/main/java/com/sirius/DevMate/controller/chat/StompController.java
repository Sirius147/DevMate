package com.sirius.DevMate.controller.chat;

import com.sirius.DevMate.controller.chat.dto.ChatMessageDto;
import com.sirius.DevMate.controller.chat.dto.SendMessageRequest;
import com.sirius.DevMate.controller.chat.dto.stomp.StompMarkRead;
import com.sirius.DevMate.controller.chat.dto.stomp.StompMessageEvent;
import com.sirius.DevMate.controller.chat.dto.stomp.StompSendMessage;
import com.sirius.DevMate.service.project.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StompController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    // 메시지 전송: /app/chat.channels.{channelId}.send
    @MessageMapping("/chat.channels.{channelId}.send")
    public void sendMessage(@DestinationVariable Long channelId,
                            @RequestBody StompSendMessage payload,
                            Principal principal) {
        Long userId = Long.valueOf(principal.getName()); // UserIdPrincipal에서 id를 String name으로 저장

        SendMessageRequest sendMessageRequest = new SendMessageRequest(
                userId,
                payload.content(),
                payload.attachmentPayloads() == null ? List.of()
                        : payload.attachmentPayloads().stream()
                        .map(a -> new SendMessageRequest.AttachmentPayload(
                                a.storageKey(), a.url(), a.filename(), a.contentType(), a.fileSize()
                        )).toList()
        );

        ChatMessageDto chatMessageDto = chatService.sendMessage(channelId, sendMessageRequest); // DB 저장

        // 채널 브로드캐스트
        var event = new StompMessageEvent(
                chatMessageDto.id(), chatMessageDto.channelId(), chatMessageDto.senderUserId(),
                chatMessageDto.messageType(), chatMessageDto.content(), chatMessageDto.createdAt(),
                chatMessageDto.attachments().stream().map(a ->
                        new StompMessageEvent.Attachment(a.id(), a.url(), a.filename(), a.contentType(), a.storageSize())
                ).toList()
        );
        simpMessagingTemplate.convertAndSend("/topic/channels." + channelId, event);

//        // 보낸 사람에게 receipt
//        simpMessagingTemplate.convertAndSendToUser(principal.getName(), "/queue/receipts",
//                Map.of("type", "SENT", "messageId", chatMessageDto.id()));
    }

    // 읽음 처리: /app/chat.channels.{channelId}.read
    @MessageMapping("/chat.channels.{channelId}.read")
    public void markRead(@DestinationVariable Long channelId,
                         @RequestBody StompMarkRead payload,
                         Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        chatService.markRead(channelId, userId, payload.lastReadMessageId());
//        // 채널 전체에 읽음 업데이트 브로드캐스트
//        StompReadReceipt stompReadReceipt = new StompReadReceipt(channelId, userId, payload.lastReadMessageId());
//        simpMessagingTemplate.convertAndSend("/topic/channels." + channelId, stompReadReceipt);
//
//        //  나에게만 확인 응답
//        simpMessagingTemplate.convertAndSendToUser(principal.getName(), "/queue/receipts",
//                Map.of("type", "READ_OK", "lastReadMessageId", payload.lastReadMessageId()));
    }
}
