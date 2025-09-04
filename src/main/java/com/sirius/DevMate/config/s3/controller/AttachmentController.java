package com.sirius.DevMate.config.s3.controller;

import com.sirius.DevMate.config.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-channels/{chatChannelId}/attachments")
public class AttachmentController {
    private final S3Service s3Service;



}
