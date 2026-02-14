package com.example.pdfwill.service;

import com.example.pdfwill.dto.ConversionResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ConversionService {
    ConversionResponseDTO convertImageToPdf(MultipartFile file);
    ConversionResponseDTO mergePdf(List<MultipartFile> files);
    ConversionResponseDTO convertPdfToImages(MultipartFile file);
}