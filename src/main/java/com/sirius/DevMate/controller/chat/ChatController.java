package com.sirius.DevMate.controller.chat;

import com.sirius.DevMate.controller.dto.chat.ChatMessageDto;
import com.sirius.DevMate.controller.dto.chat.SendMessageRequest;
import com.sirius.DevMate.domain.common.sys.Direction;
import com.sirius.DevMate.service.project.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat/channels/{channelId}")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/messages")
    public ChatMessageDto sendMessage(
            @PathVariable Long channelId,
            @RequestBody SendMessageRequest req
    ) {
        return chatService.sendMessage(channelId, req);
    }

    @GetMapping("/messages")
    public List<ChatMessageDto> listMessages(
            @PathVariable Long channelId,
            @RequestParam(required = false) Long anchorId,
            @RequestParam(defaultValue = "OLDER") Direction dir,
            @RequestParam(defaultValue = "50") int limit
    ) {
        return chatService.listMessages(channelId, anchorId, dir, Math.min(limit, 100));
    }

    @PostMapping("/read")
    public void markRead(
            @PathVariable Long channelId,
            @RequestParam Long userId,
            @RequestParam Long messageId
    ) {
        chatService.markRead(channelId, userId, messageId);
    }

    @GetMapping("/unread-count")
    public long unreadCount(
            @PathVariable Long channelId,
            @RequestParam Long userId
    ) {
        return chatService.unreadCount(channelId, userId);
    }
}
