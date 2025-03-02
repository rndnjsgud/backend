package com.arom.yeojung.service;

import com.arom.yeojung.object.File;
import com.arom.yeojung.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    //파일 이름과 url db에 저장
    //예외처리 수정해야함
    public File save(String fileName, String fileUrl) throws IOException {
        File file = new File();
        file.setFileName(fileName);
        file.setFileUrl(fileUrl);
        return fileRepository.save(file);
    }

    //파일 조회
    public File getFile(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("no file"));
    }

    //파일 조회
    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    //파일 삭제
    public void deleteFile(Long fileId) {
        fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("no file"));

        fileRepository.deleteById(fileId);
    }
}
