package com.arom.yeojung.controller;

import com.arom.yeojung.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {
    private final S3Uploader s3Uploader;

    //이미지 업로드
    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
    String imageUrl = s3Uploader.uploadFile(file, "images");
    return ResponseEntity.ok(imageUrl);
    }

    //비디오 업로드
    @PostMapping("/video")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        String videoUrl = s3Uploader.uploadFile(file, "videos");
        return ResponseEntity.ok(videoUrl);
    }
}
