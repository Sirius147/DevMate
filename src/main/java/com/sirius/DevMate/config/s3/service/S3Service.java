package com.sirius.DevMate.config.s3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String putObject(String key, InputStream inputStream, Long fileSize,
                            String contentType) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .contentLength(fileSize)
                .build();

        s3Client.putObject(request, RequestBody.fromInputStream(inputStream, fileSize));
        return key;

    }

    public byte[] getObject(String key) {
        ResponseBytes<GetObjectResponse> objectResponseResponseBytes =
                s3Client.getObjectAsBytes(GetObjectRequest.builder().bucket(bucket).key(key).build());
        return objectResponseResponseBytes.asByteArray();
    }

    public void deleteObject(String key) {
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
    }


}
