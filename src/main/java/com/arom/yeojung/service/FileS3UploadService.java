package com.arom.yeojung.service;


import com.arom.yeojung.object.File;
import com.arom.yeojung.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileS3UploadService {

    private final String bucketName = "yeojung-bucket";
    private final S3Client s3Client;
    private final FileService fileService;

    public FileS3UploadService(FileService fileService) {
        this.fileService = fileService;
        this.s3Client = S3Client.builder()
                .region(Region.AP_NORTHEAST_2) //서울 지역
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                System.getenv("AWS_ACCESS_KEY"),
                                System.getenv("AWS_SECRET_KEY"))
                ))
                .build();
    }

    public File uploadAndSaveFile(MultipartFile file) {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        String fileUrl = "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + fileName;

        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));
            return fileService.save(fileName, fileUrl);
        } catch (IOException e) {
            throw new RuntimeException("S3 Failed to upload file", e);
        }
    }
}
