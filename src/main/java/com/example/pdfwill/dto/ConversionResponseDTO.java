package com.example.pdfwill.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConversionResponseDTO {

    private String fileId;
    private String downloadUrl;
    private String status;
}