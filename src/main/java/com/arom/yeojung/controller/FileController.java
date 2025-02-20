package com.arom.yeojung.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/files")
public class FileController {

    //파일명 가져오기
    @GetMapping("/{filename}")
    public ResponseEntity<String> getFileUrl(@PathVariable String filename) {
        String fileUrl = "https://your-s3-bucket.s3.amazonaws.com/" + filename;
        return ResponseEntity.ok(fileUrl);
    }

    //DB에서 파일 삭제
    @DeleteMapping("{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
        return ResponseEntity.ok("File deleted : " + filename);
    }
}
