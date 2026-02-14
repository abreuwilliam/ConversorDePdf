package com.example.pdfwill.storege;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@Slf4j
public class TempFileStorageService {

    private final Path baseTempDir =
            Paths.get("temp").toAbsolutePath().normalize();

    private final Path tempDir = Paths.get("temp");

    @PostConstruct
    public void init() {
        try {
            if (!Files.exists(tempDir)) {
                Files.createDirectories(tempDir);
                log.info("Diretório temporário criado: {}", tempDir.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar diretório temporário", e);
        }
    }

    public Path saveTempFile(MultipartFile file) {

        try {
            String extension = getExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID() + extension;

            Path targetPath = tempDir.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            log.info("Arquivo salvo temporariamente: {}", targetPath);

            return targetPath;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar arquivo temporário", e);
        }
    }

    public Path getTempPath(String fileName) {
        return tempDir.resolve(fileName);
    }

    public void deleteFile(Path path) {
        try {
            Files.deleteIfExists(path);
            log.info("Arquivo temporário deletado: {}", path);
        } catch (IOException e) {
            log.warn("Falha ao deletar arquivo temporário: {}", path);
        }
    }

    private String getExtension(String fileName) {

        if (fileName == null || !fileName.contains(".")) {
            return "";
        }

        return fileName.substring(fileName.lastIndexOf("."));
    }
    public Path getBaseTempDir() {
        return baseTempDir;
    }
}
