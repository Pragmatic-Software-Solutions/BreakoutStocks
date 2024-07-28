package com.finance.tracker.breakout.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfManipulationService {

    public byte[] mergePdfFiles(MultipartFile file1, MultipartFile file2) throws IOException {
        // Create a PDFMergerUtility instance
        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();

        // Create a ByteArrayOutputStream to store the merged PDF
        try (ByteArrayOutputStream mergedOutputStream = new ByteArrayOutputStream()) {
            // Add the source PDF files
            pdfMergerUtility.addSource(file1.getInputStream());
            pdfMergerUtility.addSource(file2.getInputStream());

            // Set the destination stream
            pdfMergerUtility.setDestinationStream(mergedOutputStream);

            // Merge the documents
            pdfMergerUtility.mergeDocuments(null);

            // Return the merged PDF as a byte array
            return mergedOutputStream.toByteArray();
        }
    }

    public byte[] removePage(MultipartFile file, int pageNumber) throws IOException {
        // Load the PDF document
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            int totalPages = document.getNumberOfPages();

            // Check if the page number is within the valid range
            if (pageNumber < 1 || pageNumber > totalPages) {
                throw new IllegalArgumentException("Invalid page number. The PDF has " + totalPages + " pages.");
            }

            // Remove the page (pageNumber - 1 because PDFBox uses zero-based index)
            document.removePage(pageNumber - 1);

            // Save the modified PDF to a ByteArrayOutputStream
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                document.save(outputStream);
                return outputStream.toByteArray();
            }
        }
    }
}
