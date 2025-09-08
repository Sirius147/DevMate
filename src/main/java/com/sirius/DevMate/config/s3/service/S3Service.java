package com.sirius.DevMate.config.s3.service;

import com.sirius.DevMate.config.s3.AttachmentPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final AttachmentPolicy attachmentPolicy;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.s3.region}")
    private String region;

    public record PresignPutReq(String filename, String contentType, long size) {}
    public record PresignPutRes(String uploadUrl, String storageKey, Instant expiresAt) {}

    public PresignPutRes createPutUrl(Long channelId, PresignPutReq request) {
        attachmentPolicy.validateRequest(request.contentType(), request.size());
        String key = attachmentPolicy.buildObjectKey(channelId, request.filename());

        PutObjectRequest put = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(request.contentType())
                .contentLength(request.size()) // 일부 클라이언트가 넣어야 함
                .build();

        PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(b -> b
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(put));

        return new PresignPutRes(presignedPutObjectRequest.url().toString(), key,
                Instant.now().plus(Duration.ofMinutes(10)));
    }

    /** 업로드 후 서버 검증용: S3에 실제로 올라갔는지, 타입/크기 일치하는지 */
    public HeadObjectResponse head(String key) {
        return s3Client.headObject(b -> b.bucket(bucket).key(key));
    }

    /** 퍼블릭 링크 정책이면 다음처럼 URL 생성(아니라면 CloudFront/서명URL로 대체) */
    public String publicUrl(String key) {
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
    }
}
