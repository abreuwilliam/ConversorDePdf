package com.example.pdfwill.service;

import com.example.pdfwill.storege.TempFileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.nio.file.Path;
import java.time.Instant;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class TempFileCleanupJob {

    private final TempFileStorageService storageService;

    // 20000ms = 20 segundos
    @Scheduled(fixedDelay = 120000)
    public void cleanTempFolder() {
        log.info("Iniciando faxina total na pasta temp...");

        Path tempDir = storageService.getBaseTempDir();

        // Files.list lista apenas os arquivos da raiz da pasta temp
        try (Stream<Path> files = Files.list(tempDir)) {

            files.forEach(file -> {
                try {
                    // Tenta deletar sem olhar a data/hora
                    boolean deletado = Files.deleteIfExists(file);
                    if (deletado) {
                        log.info("Removido com sucesso: {}", file.getFileName());
                    }
                } catch (Exception e) {
                    log.error("Não foi possível remover o arquivo: {}", file.getFileName());
                }
            });

        } catch (Exception e) {
            log.error("Erro ao acessar a pasta temp para limpeza", e);
        }
    }
}