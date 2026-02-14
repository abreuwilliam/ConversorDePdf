package com.example.pdfwill.controller;

import com.example.pdfwill.dto.ConversionResponseDTO;
import com.example.pdfwill.service.ConversionService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/converter")
@RequiredArgsConstructor
public class ConversionController {

    private final ConversionService conversionService;

    private final Bucket bucket = Bucket.builder()
            .addLimit(
                    Bandwidth.classic(
                            20, // capacidade do balde (burst)
                            Refill.intervally(
                                    20,
                                    Duration.ofMinutes(1)
                            )
                    )
            )
            .build();

    private void validateRateLimit() {
        if (!bucket.tryConsume(1)) {
            throw new RuntimeException("Limite de requisições excedido");
        }
    }

    @PostMapping(
            value = "/jpg-to-pdf",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ConversionResponseDTO> converterJpgToPdf(
            @RequestParam("file") MultipartFile file
    ) {

        validateRateLimit();

        return ResponseEntity.ok(
                conversionService.convertImageToPdf(file)
        );
    }

    @PostMapping(
            value = "/merge-pdf",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ConversionResponseDTO> mergePdf(
            @RequestPart("files") List<MultipartFile> files
    ) {

        validateRateLimit();

        return ResponseEntity.ok(
                conversionService.mergePdf(files)
        );
    }

    @PostMapping(
            value = "/pdf-to-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ConversionResponseDTO> pdfToImage(
            @RequestParam("file") MultipartFile file
    ) {

        validateRateLimit();

        return ResponseEntity.ok(
                conversionService.convertPdfToImages(file)
        );
    }
}