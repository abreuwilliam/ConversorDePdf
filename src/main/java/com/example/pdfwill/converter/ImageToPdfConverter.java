package com.example.pdfwill.converter;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@Slf4j
public class ImageToPdfConverter {

    public void convert(Path inputImagePath, Path outputPdfPath) {

        try (PDDocument document = new PDDocument()) {

            // carregar imagem
            PDImageXObject image =
                    PDImageXObject.createFromFile(inputImagePath.toString(), document);

            // criar página com tamanho da imagem
            PDPage page = new PDPage(
                    new PDRectangle(image.getWidth(), image.getHeight())
            );

            document.addPage(page);

            // desenhar imagem no PDF
            try (PDPageContentStream contentStream =
                         new PDPageContentStream(document, page)) {

                contentStream.drawImage(
                        image,
                        0,
                        0,
                        image.getWidth(),
                        image.getHeight()
                );
            }

            // salvar PDF
            document.save(outputPdfPath.toFile());

            log.info("Conversão concluída: {}", outputPdfPath);

        } catch (IOException e) {

            log.error("Erro ao converter imagem para PDF", e);
            throw new RuntimeException("Erro ao converter imagem para PDF", e);
        }
    }
}
