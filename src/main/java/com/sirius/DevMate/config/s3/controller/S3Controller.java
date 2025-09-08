package com.sirius.DevMate.config.s3.controller;

import com.sirius.DevMate.config.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/channels/{channelId}/attachments")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/presign-put")
    public S3Service.PresignPutRes preSignPut(
            @PathVariable Long channelId,
            @RequestBody S3Service.PresignPutReq presignPutReq
            ) {
        return s3Service.createPutUrl(channelId, presignPutReq);

    }
}
