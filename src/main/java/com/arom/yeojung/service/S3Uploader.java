package com.arom.yeojung.service;


import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Uploader {

    private final String bucketName = "yeojung-bucket";
    private final S3Client s3Client;

    public S3Uploader() {
        this.s3Client = S3Client.builder()
                .region(Region.AP_NORTHEAST_2) //서울 지역
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                System.getenv("AWS_ACCESS_KEY"),
                                System.getenv("AWS_SECRET_KEY"))
                ))
                .build();
    }

    public String uploadFile(MultipartFile file, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));

            return "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("S3 Failed to upload file", e);
        }
    }

  // S3 파일 삭제
  public void deleteFile(String fileUrl) {
    String fileName = extractFileNameFromUrl(fileUrl);

    // 파일이 존재하는지 확인 후 삭제
    if (doesFileExist(fileName)) {
      s3Client.deleteObject(DeleteObjectRequest.builder()
          .bucket(bucketName)
          .key(fileName)
          .build());
      log.info("파일 삭제 완료: {}", fileUrl);
    } else {
      log.warn("파일이 존재하지 않음: {}", fileUrl);
    }
  }

  // S3 파일 존재 여부 확인
  public boolean doesFileExist(String fileUrl) {
    String fileName = extractFileNameFromUrl(fileUrl);
    try {
      s3Client.headObject(HeadObjectRequest.builder()
          .bucket(bucketName)
          .key(fileName)
          .build());
      return true;
    } catch (NoSuchKeyException e) {
      return false; // 파일이 존재하지 않음
    }
  }

  // 파일 URL에서 S3 경로 추출
  private String extractFileNameFromUrl(String fileUrl) {
    return fileUrl.replace("https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/", "");
  }
}
