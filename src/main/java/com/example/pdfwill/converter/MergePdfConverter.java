package com.example.pdfwill.converter;


import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
@Slf4j
public class MergePdfConverter {

    public void merge(List<Path> inputFiles, Path outputFile) {

        PDFMergerUtility merger = new PDFMergerUtility();

        try {

            for (Path file : inputFiles) {
                merger.addSource(file.toFile());
            }

            merger.setDestinationFileName(outputFile.toString());

            merger.mergeDocuments(null);

            log.info("PDFs unidos com sucesso: {}", outputFile);

        } catch (IOException e) {

            log.error("Erro ao unir PDFs", e);
            throw new RuntimeException("Erro ao unir PDFs", e);
        }
    }
}
