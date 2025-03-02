package com.arom.yeojung.controller;


import com.arom.yeojung.object.File;
import com.arom.yeojung.service.FileS3UploadService;
import com.arom.yeojung.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileS3UploadService fileS3UploadService;
    private final FileService fileService;

    //이미지 업로드
    //예외처리 사용자 예외 처리로 변환
    @PostMapping("/upload/image")
    public ResponseEntity<File> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        File imgfile = fileS3UploadService.uploadAndSaveFile(file);
        //fileService.save(imageUrl, fileName);
        return ResponseEntity.ok(imgfile);
    }

    //비디오 업로드
    @PostMapping("/upload/video")
    public ResponseEntity<File> uploadVideo(@RequestParam("file") MultipartFile file) throws IOException {
        File videofile = fileS3UploadService.uploadAndSaveFile(file);
        //fileService.save(fileName, videoUrl);
        return ResponseEntity.ok(videofile);
    }

    //파일 가져오기
    @GetMapping("/download/{fileId}")
    public ResponseEntity<File> getFileUrl(@PathVariable Long fileId) {
        return ResponseEntity.ok(fileService.getFile(fileId));
    }

    //DB에서 파일 삭제
    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long fileId) {
        fileService.deleteFile(fileId);
        return ResponseEntity.ok("File deleted");
    }
}