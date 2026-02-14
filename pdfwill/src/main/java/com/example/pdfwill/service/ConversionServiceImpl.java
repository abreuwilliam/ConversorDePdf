package com.example.pdfwill.service;

import com.example.pdfwill.converter.ImageToPdfConverter;
import com.example.pdfwill.converter.MergePdfConverter;
import com.example.pdfwill.converter.PdfToImageConverter;
import com.example.pdfwill.dto.ConversionResponseDTO;

import com.example.pdfwill.storege.TempFileStorageService;
import com.example.pdfwill.util.FileNameUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversionServiceImpl implements ConversionService {


    private final TempFileStorageService storageService;

    private final ImageToPdfConverter imageToPdfConverter;

    private final MergePdfConverter mergePdfConverter;

    private final PdfToImageConverter pdfToImageConverter;


    @Override
    public ConversionResponseDTO convertImageToPdf(MultipartFile file) {

        // 1️⃣ validação básica
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Arquivo inválido ou vazio");
        }

        // 2️⃣ salvar arquivo temporário
        Path inputPath = storageService.saveTempFile(file);

        // 3️⃣ gerar nome do arquivo de saída
        String outputFileName = FileNameUtil.generatePdfName();

        Path outputPath = storageService.getTempPath(outputFileName);

        // 4️⃣ executar conversão
        imageToPdfConverter.convert(inputPath, outputPath);

        // 5️⃣ gerar id de download
        String fileId = outputPath.getFileName().toString();

        // 6️⃣ montar DTO
        return ConversionResponseDTO.builder()
                .fileId(fileId)
                .downloadUrl("/download/" + fileId)
                .status("SUCCESS")
                .build();
    }

    @Override
    public ConversionResponseDTO mergePdf(List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            throw new RuntimeException("Nenhum arquivo enviado");
        }

        // salvar arquivos temp
        List<Path> inputPaths = files.stream()
                .map(storageService::saveTempFile)
                .toList();

        // gerar nome saída
        String outputFileName = FileNameUtil.generatePdfName();
        Path outputPath = storageService.getTempPath(outputFileName);

        // executar merge
        mergePdfConverter.merge(inputPaths, outputPath);

        String fileId = outputPath.getFileName().toString();

        return ConversionResponseDTO.builder()
                .fileId(fileId)
                .downloadUrl("/download/" + fileId)
                .status("SUCCESS")
                .build();
    }

    @Override
    public ConversionResponseDTO convertPdfToImages(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Arquivo inválido");
        }

        // 1️⃣ Salva o PDF original temporariamente
        Path inputPath = storageService.saveTempFile(file);

        // 2️⃣ Gera um nome de arquivo único para a imagem (ex: uuid.png)
        // Usamos o utilitário para gerar um nome e trocamos a extensão
        String outputFileName = FileNameUtil.generatePdfName().replace(".pdf", ".png");
        Path outputPath = storageService.getTempPath(outputFileName);

        // 3️⃣ Executa a conversão passando o caminho do ARQUIVO, não da pasta
        // Nota: Você precisará ajustar o PdfToImageConverter para aceitar o caminho do arquivo
        pdfToImageConverter.convert(inputPath, outputPath);

        // 4️⃣ Limpa o PDF original
        storageService.deleteFile(inputPath);

        String fileId = outputPath.getFileName().toString();

        return ConversionResponseDTO.builder()
                .fileId(fileId)
                .downloadUrl("/download/" + fileId)
                .status("SUCCESS")
                .build();
    }
}