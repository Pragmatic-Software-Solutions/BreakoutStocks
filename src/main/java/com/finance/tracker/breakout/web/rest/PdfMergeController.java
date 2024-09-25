package com.finance.tracker.breakout.web.rest;

import com.finance.tracker.breakout.service.PdfManipulationService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pdf")
public class PdfMergeController {

    @Autowired
    private PdfManipulationService pdfManipulationService;

    @PostMapping(value = "/merge", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> mergePdfFiles(@RequestPart("file1") MultipartFile file1, @RequestPart("file2") MultipartFile file2) {
        try {
            // Merge the PDF files
            byte[] mergedPdf = pdfManipulationService.mergePdfFiles(file1, file2);

            // Return the merged PDF as a response
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=merged.pdf");
            return new ResponseEntity<>(mergedPdf, headers, HttpStatus.OK);
        } catch (IOException e) {
            // Handle the exception if any errors occur during merging
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/remove-page", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> removePageFromPdf(@RequestPart("file") MultipartFile file, @RequestParam("pageNumber") int pageNumber) {
        try {
            // Remove the specified page from the PDF
            byte[] modifiedPdf = pdfManipulationService.removePage(file, pageNumber);

            // Return the modified PDF as a response
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=modified.pdf");
            return new ResponseEntity<>(modifiedPdf, headers, HttpStatus.OK);
        } catch (IOException e) {
            // Handle any IOException that occurs during file manipulation
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            // Handle invalid page number error
            return new ResponseEntity<>(e.getMessage().getBytes(), HttpStatus.BAD_REQUEST);
        }
    }
}
