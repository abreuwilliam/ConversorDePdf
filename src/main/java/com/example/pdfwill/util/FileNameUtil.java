package com.example.pdfwill.util;

import java.util.UUID;

public final class FileNameUtil {

    private FileNameUtil() {
        // evitar instanciação
    }

    public static String generatePdfName() {
        return UUID.randomUUID() + ".pdf";
    }

    public static String generateTempName(String extension) {

        if (extension == null || extension.isBlank()) {
            extension = "";
        }

        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }

        return UUID.randomUUID() + extension;
    }

    public static String sanitizeFileName(String originalName) {

        if (originalName == null) {
            return "file";
        }

        return originalName
                .replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }

    public static String extractExtension(String fileName) {

        if (fileName == null || !fileName.contains(".")) {
            return "";
        }

        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}