package com.sirius.DevMate.controller.dto.chat;

import java.util.List;

// 요청: 메시지 전송 (TEXT 기준; 첨부는 메타데이터 리스트 포함)
public record SendMessageRequest(
        Long userId,                 // 보낸 사람(검증에 사용)
        String content,              // TEXT일 경우 내용
        List<AttachmentPayload> attachments // 이미 업로드된 파일의 메타
) {
    public record AttachmentPayload(
        String storageKey,
        String url,
        String filename,
        String contentType,
        Long fileSize
){}
}
