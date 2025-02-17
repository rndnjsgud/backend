package com.arom.yeojung.service;

import com.arom.yeojung.object.File;
import com.arom.yeojung.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    public File save(String fileName, String fileUrl) throws IOException {
        File file = new File();
        file.setFileName(fileName);
        file.setFileUrl(fileUrl);
        return fileRepository.save(file);
    }
}
