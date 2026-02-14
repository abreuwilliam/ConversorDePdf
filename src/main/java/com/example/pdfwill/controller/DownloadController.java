package com.example.pdfwill.controller;


import com.example.pdfwill.storege.TempFileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/download")
@RequiredArgsConstructor
public class DownloadController {

    private final TempFileStorageService storageService;

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String fileId
    ) {

        // 🔒 proteção básica
        if (fileId.contains("..")) {
            throw new RuntimeException("Arquivo inválido");
        }

        Path filePath = storageService.getTempPath(fileId);

        try {

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(filePath);

            String contentType = Files.probeContentType(filePath);

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileId + "\""
                    )
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao realizar download", e);
        }
    }

}