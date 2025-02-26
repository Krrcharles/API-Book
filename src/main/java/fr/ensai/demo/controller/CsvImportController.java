package fr.ensai.demo.controller;

import fr.ensai.demo.service.CsvImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "3 Import with CSV", description = "Upload a csv file to fill the database")
@RestController
@RequestMapping("/csv")
public class CsvImportController {

    @Autowired
    private CsvImportService csvImportService;

    @Operation(summary = "Import books from CSV", description = "Uploads a CSV file containing book data")
    @PostMapping(value = "/importBooks", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importBooks(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "charsetName", defaultValue = "windows-1252") String charsetName) {
        try {
            // Create a temporary file to store the uploaded CSV
            File tempFile = File.createTempFile("books", ".csv");
            file.transferTo(tempFile);

            // Call the service to import books from the CSV file
            csvImportService.importBooksFromCsv(tempFile, charsetName);

            return ResponseEntity.ok("Books imported successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to import books: " + e.getMessage());
        }
    }
}
