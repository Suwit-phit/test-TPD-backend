package com.example.matching.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.matching.model.AttactmentsOutside;
import com.example.matching.service.AttactmentsOutsideService;

// import com.example.userCanDoTheirOwnCRUD.model.AttactmentsOutside;
// import com.example.userCanDoTheirOwnCRUD.service.AttactmentsOutsideService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attachments-outside")
// @CrossOrigin(origins = "http://localhost:5173")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class AttactmentsOutsideController {

    private final AttactmentsOutsideService attactmentsOutsideService;

    public AttactmentsOutsideController(AttactmentsOutsideService attactmentsOutsideService) {
        this.attactmentsOutsideService = attactmentsOutsideService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            attactmentsOutsideService.uploadAttachment(file);
            return new ResponseEntity<>("Upload successfully", HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<AttactmentsOutside>> getAllAttachments(
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "ascending", defaultValue = "false") boolean ascending) {
        try {
            List<AttactmentsOutside> attachments = attactmentsOutsideService.getAllAttachments(sortBy, ascending);
            return ResponseEntity.ok(attachments);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Modified method to download based on UUID and file name
    @GetMapping("/{id}/download/{fileName}")
    public ResponseEntity<?> downloadAttachment(@PathVariable UUID id, @PathVariable String fileName) {
        try {
            AttactmentsOutside attachment = attactmentsOutsideService.getAttachment(id, fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(attachment.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + attachment.getFileName() + "\"")
                    .body(attachment.getData());
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Failed to download file: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Modified method to delete based on UUID and file name
    // @DeleteMapping("/{id}/delete/{fileName}")
    // public ResponseEntity<String> deleteAttachment(@PathVariable UUID id,
    // @PathVariable String fileName) {
    // try {
    // attactmentsOutsideService.deleteAttachment(id, fileName);
    // return new ResponseEntity<>("Delete successfully", HttpStatus.NO_CONTENT);
    // } catch (Exception e) {
    // return new ResponseEntity<>("Failed to delete file: " + e.getMessage(),
    // HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }
    @DeleteMapping("/{id}/delete/{fileName}")
    public ResponseEntity<String> deleteAttachment(@PathVariable UUID id, @PathVariable String fileName) {
        try {
            attactmentsOutsideService.deleteAttachment(id, fileName);
            // Return HTTP 200 OK with the desired message format
            return new ResponseEntity<>("Delete attachment \"" + fileName + "\" successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
