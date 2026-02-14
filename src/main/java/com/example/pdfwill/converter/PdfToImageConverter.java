package com.example.pdfwill.converter;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

@Component
@Slf4j
public class PdfToImageConverter {

    /**
     * Converte a primeira página do PDF em uma imagem única.
     * Salva diretamente no local especificado pelo outputPath.
     */
    public void convert(Path inputPdf, Path outputPath) {

        try (PDDocument document = Loader.loadPDF(inputPdf.toFile())) {

            PDFRenderer renderer = new PDFRenderer(document);

            // Renderiza apenas a primeira página (índice 0)
            // 300 DPI garante uma boa qualidade de leitura
            BufferedImage image = renderer.renderImageWithDPI(0, 300);

            // Salva a imagem diretamente no arquivo destino (na pasta temp)
            ImageIO.write(image, "PNG", outputPath.toFile());

            log.info("PDF convertido para imagem única: {}", outputPath.getFileName());

        } catch (IOException e) {
            log.error("Erro ao converter PDF para imagem", e);
            throw new RuntimeException("Erro ao converter PDF para imagem", e);
        }
    }
}