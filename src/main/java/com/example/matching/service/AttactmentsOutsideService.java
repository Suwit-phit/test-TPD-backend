package com.example.matching.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.matching.model.AttactmentsOutside;
import com.example.matching.model.Candidate;
import com.example.matching.model.Candidate.Attachment;
import com.example.matching.repository.AttactmentsOutsideRepository;
import com.example.matching.repository.CandidateRepository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

@Service
public class AttactmentsOutsideService {

    private final AttactmentsOutsideRepository attactmentsOutsideRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    public AttactmentsOutsideService(AttactmentsOutsideRepository attactmentsOutsideRepository) {
        this.attactmentsOutsideRepository = attactmentsOutsideRepository;
    }

    public AttactmentsOutside uploadAttachment(MultipartFile file) throws IOException {
        AttactmentsOutside attachment = AttactmentsOutside.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .data(file.getBytes())
                .build();
        return attactmentsOutsideRepository.save(attachment);
    }

    public List<AttactmentsOutside> getAllAttachments(String sortBy, boolean ascending) {
        List<AttactmentsOutside> outsideAttachments = attactmentsOutsideRepository.findAll();
    
        // Convert UserProfile's Attachment to AttactmentsOutside with proper field setting
        List<AttactmentsOutside> candidateAttachments = new ArrayList<>();
        candidateRepository.findAll().forEach(candidate -> {
            for (Attachment attachment : candidate.getAttachments()) {
                AttactmentsOutside outside = new AttactmentsOutside();
                outside.setId(candidate.getId());
                outside.setFileName(attachment.getFileName());
                outside.setFileType(determineFileType(attachment.getFileName()));
                outside.setData(attachment.getFileData());
                outside.setCreatedAt(attachment.getCreatedAt()); // If UserProfile's Attachment has createdAt
                candidateAttachments.add(outside);
            }
        });
    
        // Combine both lists
        outsideAttachments.addAll(candidateAttachments);
    
        // Sort with null handling
        outsideAttachments.sort((a1, a2) -> {
            // Handle sorting based on provided parameters
            if ("fileName".equals(sortBy)) {
                return ascending ? 
                    a1.getFileName().compareToIgnoreCase(a2.getFileName()) : 
                    a2.getFileName().compareToIgnoreCase(a1.getFileName());
            } else if ("createdAt".equals(sortBy)) {
                // Sorting by createdAt, nulls last
                if (a1.getCreatedAt() == null && a2.getCreatedAt() == null) return 0;
                if (a1.getCreatedAt() == null) return ascending ? 1 : -1;
                if (a2.getCreatedAt() == null) return ascending ? -1 : 1;
                return ascending ? 
                    a1.getCreatedAt().compareTo(a2.getCreatedAt()) : 
                    a2.getCreatedAt().compareTo(a1.getCreatedAt());
            } 
            return 0; // Default case if no recognized sort field
        });
    
        return outsideAttachments;
    }
    
    // Helper method to determine file type from file name
    private String determineFileType(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            switch (extension) {
                case "pdf":
                    return "application/pdf";
                case "png":
                    return "image/png";
                case "jpg":
                case "jpeg":
                    return "image/jpeg";
                case "doc":
                case "docx":
                    return "application/msword";
                default:
                    return "application/octet-stream"; // Generic type if unknown
            }
        }
        return "application/octet-stream"; // Default file type if not determined
    }

    public AttactmentsOutside getAttachment(UUID id, String fileName) {
        // First, try to find in AttactmentsOutside
        AttactmentsOutside attachment = attactmentsOutsideRepository.findById(id)
                .filter(att -> fileName.equals(att.getFileName()))
                .orElse(null);
    
        if (attachment == null) {
            // If not found, search in UserProfile's attachments
            for (Candidate candidate : candidateRepository.findAll()) {
                if (candidate.getId().equals(id)) {
                    for (Attachment userAttachment : candidate.getAttachments()) {
                        if (fileName.equals(userAttachment.getFileName())) {
                            return AttactmentsOutside.builder()
                                    .id(id)
                                    .fileName(userAttachment.getFileName())
                                    .fileType(determineFileType(userAttachment.getFileName()))
                                    .data(userAttachment.getFileData())
                                    .build();
                        }
                    }
                }
            }
            throw new RuntimeException("Attachment not found");
        }
    
        return attachment;
    }
    

    public void deleteAttachment(UUID id, String fileName) {
        // First, try to delete in AttactmentsOutside
        AttactmentsOutside attachment = attactmentsOutsideRepository.findById(id)
                .filter(att -> fileName.equals(att.getFileName()))
                .orElse(null);

        if (attachment != null) {
            attactmentsOutsideRepository.delete(attachment);
        } else {
            // If not found in AttactmentsOutside, try to remove from UserProfile
            for (Candidate candidate : candidateRepository.findAll()) {
                if (candidate.getId().equals(id)) {
                    // Use a different parameter name to avoid conflict
                    boolean removed = candidate.getAttachments()
                            .removeIf(userAttachment -> fileName.equals(userAttachment.getFileName()));
                    if (removed) {
                        candidateRepository.save(candidate);
                        return;
                    }
                }
            }
            throw new RuntimeException("Attachment not found for deletion");
        }
    }

}
