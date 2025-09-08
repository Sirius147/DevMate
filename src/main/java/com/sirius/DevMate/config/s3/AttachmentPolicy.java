package com.sirius.DevMate.config.s3;

import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Component
public class AttachmentPolicy {
    private static final Set<String> ALLOWED = Set.of(
            "image/png","image/jpeg","application/pdf","text/plain"
    );
    private static final long MAX_SIZE = 10 * 1024 * 1024L;

    public void validateRequest(String contentType, long size) {
        if (!ALLOWED.contains(contentType)) {
            throw new IllegalArgumentException("허용되지 않은 파일 형식입니다.");
        }
        if (size <= 0 || size > MAX_SIZE) {
            throw new IllegalArgumentException("파일 크기 제한(10MB)을 초과했습니다.");
        }
    }

    public String buildObjectKey(Long channelId, String filename) {
        var now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        var safe = filename.replaceAll("[^\\w.\\-]", "_");
        return String.format("channels/%d/%d/%02d/%02d/%s_%s",
                channelId, now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
                UUID.randomUUID(), safe);
    }
}
