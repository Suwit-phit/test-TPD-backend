package com.example.matching.controller;

import com.example.matching.dto.AttachmentDTO;
import com.example.matching.dto.ContactTypeDTO;
import com.example.matching.dto.ImageDTO;
import com.example.matching.dto.CandidateDTO;
import com.example.matching.model.ContactType;
import com.example.matching.model.Candidate;
import com.example.matching.model.Candidate.Attachment;
import com.example.matching.repository.CandidateRepository;
import com.example.matching.service.CandidateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.example.matching.model.Candidate.Image;

// @CrossOrigin(origins = "http://localhost:5174")
// @CrossOrigin(origins = "http://localhost:5173")

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174" })
public class CandidateController {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CandidateService candidateService;

    //TODO TestAPI after deploy to GCP
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the API!";
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchCandidates(@RequestParam(required = false) String query) {
        try {
            // If query is null or empty, fetch all candidates
            if (query == null || query.trim().isEmpty()) {
                List<Candidate> allCandidates = candidateService.getAllCandidates();
                return new ResponseEntity<>(allCandidates, HttpStatus.OK);
            }

            List<String> searchTerms = Arrays.stream(query.split("[,\\s+and\\s+]"))
                    .map(String::trim)
                    .filter(term -> !term.isEmpty())
                    .collect(Collectors.toList());

            List<Candidate> matchingCandidates = candidateService.searchCandidates(searchTerms);
            return new ResponseEntity<>(matchingCandidates, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            // Use single quotes around the query, will appear as double quotes in JSON
            // response
            errorResponse.put("error",
                    "Backend Message Failed to search '" + query + "' candidates: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // Use 404 for not found
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to search candidates: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<Candidate> createCandidate(@RequestBody Candidate candidate) {
        Candidate savedCandidate = candidateRepository.save(candidate);
        return new ResponseEntity<>(savedCandidate, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadFile(@PathVariable UUID id,
            @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload",
                    HttpStatus.BAD_REQUEST);
        }

        try {
            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename();

            Candidate candidate = candidateRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Candidate not found"));
            candidate.getAttachments().add(new Attachment(fileName, bytes));
            candidateRepository.save(candidate);

            return new ResponseEntity<>("You successfully uploaded '" + fileName + "'",
                    HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while uploading the file",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<String> uploadImage(@PathVariable UUID id,
            @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select an image to upload",
                    HttpStatus.BAD_REQUEST);
        }

        try {
            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename();

            Candidate candidate = candidateRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Candidate not found"));
            candidate.getImages().add(new Image(fileName, bytes));
            candidateRepository.save(candidate);

            return new ResponseEntity<>("You successfully uploaded '" + fileName + "'",
                    HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while uploading the image",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<CandidateDTO>> getAllCandidates() {
        List<Candidate> candidates = candidateRepository.findAllByOrderByUpdatedAtDesc();
        List<CandidateDTO> candidateDTOs = candidates.stream().map(this::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(candidateDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateDTO> getCandidateById(@PathVariable UUID id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Candidate not found"));
        CandidateDTO candidateDTO = convertToDTO(candidate);
        return new ResponseEntity<>(candidateDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidate> updateCandidate(@PathVariable UUID id,
            @RequestBody Candidate candidateDetails) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Candidate not found"));

        candidate.setCourtesyTitle(candidateDetails.getCourtesyTitle());
        candidate.setCandidateName(candidateDetails.getCandidateName());
        // candidate.setUsername(candidateDetails.getUsername());
        candidate.setDateOfBirth(candidateDetails.getDateOfBirth());
        candidate.setPosition(candidateDetails.getPosition());
        candidate.setSalary(candidateDetails.getSalary());
        candidate.setEmploymentType(candidateDetails.getEmploymentType());
        candidate.setIndustry(candidateDetails.getIndustry());
        candidate.setPhoneNumber(candidateDetails.getPhoneNumber());
        candidate.setEmail(candidateDetails.getEmail());
        candidate.setEducationLevels(candidateDetails.getEducationLevels());
        candidate.setSkills(candidateDetails.getSkills());
        candidate.setContactTypes(candidateDetails.getContactTypes());
        candidate.setAttachments(candidateDetails.getAttachments());

        Candidate updatedCandidate = candidateRepository.save(candidate);
        return new ResponseEntity<>(updatedCandidate, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCandidate(@PathVariable UUID id) {
        try {
            Candidate candidate = candidateRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Candidate not found"));
            candidateRepository.delete(candidate);
            return new ResponseEntity<>("Candidate successfully deleted", HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred while deleting the candidate",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/update-attachment")
    public ResponseEntity<String> updateAttachment(@PathVariable UUID id,
            @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload",
                    HttpStatus.BAD_REQUEST);
        }

        try {
            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename();

            // Find the candidate and update the attachment
            Candidate candidate = candidateRepository.findById(id).orElseThrow();
            List<Attachment> attachments = candidate.getAttachments();
            boolean attachmentExists = false;

            for (Attachment attachment : attachments) {
                if (attachment.getFileName().equals(fileName)) {
                    attachment.setFileData(bytes);
                    attachmentExists = true;
                    break;
                }
            }

            if (!attachmentExists) {
                candidate.getAttachments().add(new Attachment(fileName, bytes));
            }

            candidateRepository.save(candidate);

            return new ResponseEntity<>("Attachment updated successfully",
                    HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/attachments/{fileName}")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable UUID id,
            @PathVariable String fileName) {
        try {
            Candidate candidate = candidateRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Candidate not found"));

            Attachment attachmentToDownload = candidate.getAttachments().stream()
                    .filter(attachment -> attachment.getFileName().equals(fileName))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Attachment not found"));

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(attachmentToDownload.getFileData());
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(("Error: " + e.getMessage()).getBytes(),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while downloading the file".getBytes(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/images/{fileName}")
    public ResponseEntity<byte[]> viewImage(@PathVariable UUID id, @PathVariable String fileName) {
        try {
            Candidate candidate = candidateRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Candidate not found"));

            // Find the image
            Image image = candidate.getImages().stream()
                    .filter(images -> images.getFileName().equals(fileName))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Image not found"));

            // Determine content type based on file extension
            String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            if (fileName.endsWith(".png")) {
                contentType = MediaType.IMAGE_PNG_VALUE;
            } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                contentType = MediaType.IMAGE_JPEG_VALUE;
            } else if (fileName.endsWith(".gif")) {
                contentType = MediaType.IMAGE_GIF_VALUE;
            }

            // Encode the filename to handle special characters
            String encodedFileName = UriUtils.encodePathSegment(fileName, "UTF-8");

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" +
                            encodedFileName + "\"")
                    .body(image.getFileData());
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(("Error: " + e.getMessage()).getBytes(),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while retrieving the image".getBytes(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/images/{fileName}")
    public ResponseEntity<String> updateImage(@PathVariable UUID id,
            @PathVariable String fileName,
            @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select an image to upload",
                    HttpStatus.BAD_REQUEST);
        }

        try {
            byte[] bytes = file.getBytes();
            String newFileName = file.getOriginalFilename();

            Candidate userProfile = candidateRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("User profile not found"));

            Image imageToUpdate = userProfile.getImages().stream()
                    .filter(image -> image.getFileName().equals(fileName))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Image not found"));

            imageToUpdate.setFileName(newFileName);
            imageToUpdate.setFileData(bytes);
            candidateRepository.save(userProfile);

            return new ResponseEntity<>("Image updated successfully", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while updating the image",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/images/{fileName}")
    public ResponseEntity<String> deleteImage(@PathVariable UUID id,
            @PathVariable String fileName) {
        try {
            Candidate userProfile = candidateRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("User profile not found"));

            boolean removed = userProfile.getImages().removeIf(image -> image.getFileName().equals(fileName));

            if (!removed) {
                throw new NoSuchElementException("Image not found");
            }

            candidateRepository.save(userProfile);

            return new ResponseEntity<>("Image deleted successfully", HttpStatus.OK);
        } catch (NoSuchElementException e) {

            return new ResponseEntity<>(("Error HttpStatus.NOT_FOUND: " +
                    e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>(("Error HttpStatus.INTERNAL_SERVER_ERROR: " +
                    e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}/attachments/{fileName}")
    public ResponseEntity<String> deleteAttachment(@PathVariable UUID id,
            @PathVariable String fileName) {
        try {
            Candidate userProfile = candidateRepository.findById(id).orElseThrow();
            List<Attachment> attachments = userProfile.getAttachments();

            // Find the attachment by file name
            Attachment attachmentToDelete = null;
            for (Attachment attachment : attachments) {
                if (attachment.getFileName().equals(fileName)) {
                    attachmentToDelete = attachment;
                    break;
                }
            }

            // If the attachment was found, remove it
            if (attachmentToDelete != null) {
                attachments.remove(attachmentToDelete);
                candidateRepository.save(userProfile);
                return new ResponseEntity<>("Attachment deleted successfully",
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Attachment not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private CandidateDTO convertToDTO(Candidate candidate) {
        CandidateDTO dto = new CandidateDTO();
        dto.setId(candidate.getId());
        dto.setCourtesyTitle(candidate.getCourtesyTitle());
        dto.setCandidateName(candidate.getCandidateName()); // Updated field
        dto.setDateOfBirth(candidate.getDateOfBirth());
        dto.setPosition(candidate.getPosition());
        dto.setSalary(candidate.getSalary());
        dto.setEmploymentType(candidate.getEmploymentType());
        dto.setIndustry(candidate.getIndustry());
        dto.setPhoneNumber(candidate.getPhoneNumber());
        dto.setEmail(candidate.getEmail());
        dto.setEducationLevels(candidate.getEducationLevels());
        dto.setSkills(candidate.getSkills());

        // Convert contact types (handle 'url' if it exists or not)
        List<ContactTypeDTO> contactTypeDTOs = candidate.getContactTypes().stream()
                .map(contactType -> new ContactTypeDTO(contactType.getType(), contactType.getUrl())) // Make sure 'url'
                                                                                                     // exists in
                                                                                                     // ContactType
                .collect(Collectors.toList());
        dto.setContactTypes(contactTypeDTOs);

        // Convert attachments
        List<AttachmentDTO> attachmentDTOs = candidate.getAttachments().stream()
                .map(attachment -> new AttachmentDTO(attachment.getFileName(),
                        "/api/candidates/" + candidate.getId() + "/attachments/" + attachment.getFileName()))
                .collect(Collectors.toList());
        dto.setAttachments(attachmentDTOs);

        // Convert images
        List<ImageDTO> imageDTOs = candidate.getImages().stream()
                .map(image -> new ImageDTO(image.getFileName(),
                        "/api/candidates/" + candidate.getId() + "/images/" + image.getFileName()))
                .collect(Collectors.toList());
        dto.setImages(imageDTOs);

        return dto;
    }

}

// ! Below code is the same with upper code but with comments
// package com.example.matching.controller;

// import com.example.matching.dto.AttachmentDTO;
// import com.example.matching.dto.ContactTypeDTO;
// import com.example.matching.dto.ImageDTO;
// import com.example.matching.dto.CandidateDTO;
// import com.example.matching.model.ContactType;
// import com.example.matching.model.Candidate;
// import com.example.matching.model.Candidate.Attachment;
// import com.example.matching.repository.CandidateRepository;
// import com.example.matching.service.CandidateService;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.util.UriUtils;

// import java.io.IOException;
// import java.util.List;
// import java.util.UUID;
// import java.util.Optional;
// import java.util.stream.Collectors;
// import java.util.NoSuchElementException;
// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.Map;

// import com.example.matching.model.Candidate.Image;

// @RestController
// @RequestMapping("/api/candidates")
// // @CrossOrigin(origins = "http://localhost:5174")
// // @CrossOrigin(origins = "http://localhost:5173")
// @CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
// public class CandidateController {

// @Autowired
// private CandidateRepository candidateRepository;

// @Autowired
// private CandidateService candidateService;

// // @GetMapping("/search")
// // public ResponseEntity<Object> searchCandidates(@RequestParam String query)
// {
// // try {
// // List<String> searchTerms = Arrays.stream(query.split("[,\\s+and\\s+]"))
// // .map(String::trim)
// // .filter(term -> !term.isEmpty())
// // .collect(Collectors.toList());

// // List<Candidate> matchingCandidates =
// // candidateService.searchCandidates(searchTerms);
// // return new ResponseEntity<>(matchingCandidates, HttpStatus.OK);
// // } catch (Exception e) {
// // Map<String, Object> errorResponse = new HashMap<>();
// // errorResponse.put("error", "Failed to search candidates: " +
// e.getMessage());
// // return
// //
// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
// // }
// // }
// @GetMapping("/search")
// public ResponseEntity<Object> searchCandidates(@RequestParam(required =
// false) String query) {
// try {
// // If query is null or empty, fetch all candidates
// if (query == null || query.trim().isEmpty()) {
// List<Candidate> allCandidates = candidateService.getAllCandidates();
// return new ResponseEntity<>(allCandidates, HttpStatus.OK);
// }

// List<String> searchTerms = Arrays.stream(query.split("[,\\s+and\\s+]"))
// .map(String::trim)
// .filter(term -> !term.isEmpty())
// .collect(Collectors.toList());

// List<Candidate> matchingCandidates =
// candidateService.searchCandidates(searchTerms);
// return new ResponseEntity<>(matchingCandidates, HttpStatus.OK);
// } catch (Exception e) {
// Map<String, Object> errorResponse = new HashMap<>();
// errorResponse.put("error", "Failed to search candidates: " + e.getMessage());
// return
// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
// }
// }

// @PostMapping
// public ResponseEntity<Candidate> createCandidate(@RequestBody Candidate
// candidate) {
// Candidate savedCandidate = candidateRepository.save(candidate);
// return new ResponseEntity<>(savedCandidate, HttpStatus.CREATED);
// }

// @PostMapping("/{id}/upload")
// public ResponseEntity<String> uploadFile(@PathVariable UUID id,
// @RequestParam("file") MultipartFile file) {
// if (file.isEmpty()) {
// return new ResponseEntity<>("Please select a file to upload",
// HttpStatus.BAD_REQUEST);
// }

// try {
// byte[] bytes = file.getBytes();
// String fileName = file.getOriginalFilename();

// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new NoSuchElementException("Candidate not found"));
// candidate.getAttachments().add(new Attachment(fileName, bytes));
// candidateRepository.save(candidate);

// return new ResponseEntity<>("You successfully uploaded '" + fileName + "'",
// HttpStatus.OK);
// } catch (IOException e) {
// e.printStackTrace();
// return new ResponseEntity<>("An error occurred while uploading the file",
// HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// @PostMapping("/{id}/upload-image")
// public ResponseEntity<String> uploadImage(@PathVariable UUID id,
// @RequestParam("file") MultipartFile file) {
// if (file.isEmpty()) {
// return new ResponseEntity<>("Please select an image to upload",
// HttpStatus.BAD_REQUEST);
// }

// try {
// byte[] bytes = file.getBytes();
// String fileName = file.getOriginalFilename();

// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new NoSuchElementException("Candidate not found"));
// candidate.getImages().add(new Image(fileName, bytes));
// candidateRepository.save(candidate);

// return new ResponseEntity<>("You successfully uploaded '" + fileName + "'",
// HttpStatus.OK);
// } catch (IOException e) {
// e.printStackTrace();
// return new ResponseEntity<>("An error occurred while uploading the image",
// HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// @GetMapping
// public ResponseEntity<List<CandidateDTO>> getAllCandidates() {
// List<Candidate> candidates =
// candidateRepository.findAllByOrderByUpdatedAtDesc();
// List<CandidateDTO> candidateDTOs =
// candidates.stream().map(this::convertToDTO).collect(Collectors.toList());
// return new ResponseEntity<>(candidateDTOs, HttpStatus.OK);
// }

// @GetMapping("/{id}")
// public ResponseEntity<CandidateDTO> getCandidateById(@PathVariable UUID id) {
// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new NoSuchElementException("Candidate not found"));
// CandidateDTO candidateDTO = convertToDTO(candidate);
// return new ResponseEntity<>(candidateDTO, HttpStatus.OK);
// }

// @PutMapping("/{id}")
// public ResponseEntity<Candidate> updateCandidate(@PathVariable UUID id,
// @RequestBody Candidate candidateDetails) {
// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new NoSuchElementException("Candidate not found"));

// candidate.setCourtesyTitle(candidateDetails.getCourtesyTitle());
// candidate.setCandidateName(candidateDetails.getCandidateName());
// // candidate.setUsername(candidateDetails.getUsername());
// candidate.setDateOfBirth(candidateDetails.getDateOfBirth());
// candidate.setPosition(candidateDetails.getPosition());
// candidate.setSalary(candidateDetails.getSalary());
// candidate.setEmploymentType(candidateDetails.getEmploymentType());
// candidate.setIndustry(candidateDetails.getIndustry());
// candidate.setPhoneNumber(candidateDetails.getPhoneNumber());
// candidate.setEmail(candidateDetails.getEmail());
// candidate.setEducationLevels(candidateDetails.getEducationLevels());
// candidate.setSkills(candidateDetails.getSkills());
// candidate.setContactTypes(candidateDetails.getContactTypes());
// candidate.setAttachments(candidateDetails.getAttachments());

// Candidate updatedCandidate = candidateRepository.save(candidate);
// return new ResponseEntity<>(updatedCandidate, HttpStatus.OK);
// }

// // @DeleteMapping("/{id}")
// // public ResponseEntity<String> deleteCandidate(@PathVariable UUID id) {
// // Candidate candidate = candidateRepository.findById(id)
// // .orElseThrow(() -> new NoSuchElementException("Candidate not found"));
// // candidateRepository.delete(candidate);
// // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
// // }
// @DeleteMapping("/{id}")
// public ResponseEntity<String> deleteCandidate(@PathVariable UUID id) {
// try {
// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new NoSuchElementException("Candidate not found"));
// candidateRepository.delete(candidate);
// return new ResponseEntity<>("Candidate successfully deleted", HttpStatus.OK);
// } catch (NoSuchElementException ex) {
// return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
// } catch (Exception ex) {
// return new ResponseEntity<>("An error occurred while deleting the candidate",
// HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// @PutMapping("/{id}/update-attachment")
// public ResponseEntity<String> updateAttachment(@PathVariable UUID id,
// @RequestParam("file") MultipartFile file) {
// if (file.isEmpty()) {
// return new ResponseEntity<>("Please select a file to upload",
// HttpStatus.BAD_REQUEST);
// }

// try {
// byte[] bytes = file.getBytes();
// String fileName = file.getOriginalFilename();

// // Find the candidate and update the attachment
// Candidate candidate = candidateRepository.findById(id).orElseThrow();
// List<Attachment> attachments = candidate.getAttachments();
// boolean attachmentExists = false;

// for (Attachment attachment : attachments) {
// if (attachment.getFileName().equals(fileName)) {
// attachment.setFileData(bytes);
// attachmentExists = true;
// break;
// }
// }

// if (!attachmentExists) {
// candidate.getAttachments().add(new Attachment(fileName, bytes));
// }

// candidateRepository.save(candidate);

// return new ResponseEntity<>("Attachment updated successfully",
// HttpStatus.OK);
// } catch (IOException e) {
// e.printStackTrace();
// return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// @GetMapping("/{id}/attachments/{fileName}")
// public ResponseEntity<byte[]> downloadAttachment(@PathVariable UUID id,
// @PathVariable String fileName) {
// try {
// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new NoSuchElementException("Candidate not found"));

// Attachment attachmentToDownload = candidate.getAttachments().stream()
// .filter(attachment -> attachment.getFileName().equals(fileName))
// .findFirst()
// .orElseThrow(() -> new NoSuchElementException("Attachment not found"));

// return ResponseEntity.ok()
// .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
// .body(attachmentToDownload.getFileData());
// } catch (NoSuchElementException e) {
// return new ResponseEntity<>(("Error: " + e.getMessage()).getBytes(),
// HttpStatus.NOT_FOUND);
// } catch (Exception e) {
// e.printStackTrace();
// return new ResponseEntity<>("An error occurred while downloading the
// file".getBytes(),
// HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// @GetMapping("/{id}/images/{fileName}")
// public ResponseEntity<byte[]> viewImage(@PathVariable UUID id, @PathVariable
// String fileName) {
// try {
// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new NoSuchElementException("Candidate not found"));

// // Find the image
// Image image = candidate.getImages().stream()
// .filter(images -> images.getFileName().equals(fileName))
// .findFirst()
// .orElseThrow(() -> new NoSuchElementException("Image not found"));

// // Determine content type based on file extension
// String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
// if (fileName.endsWith(".png")) {
// contentType = MediaType.IMAGE_PNG_VALUE;
// } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
// contentType = MediaType.IMAGE_JPEG_VALUE;
// } else if (fileName.endsWith(".gif")) {
// contentType = MediaType.IMAGE_GIF_VALUE;
// }

// // Encode the filename to handle special characters
// String encodedFileName = UriUtils.encodePathSegment(fileName, "UTF-8");

// return ResponseEntity.ok()
// .contentType(MediaType.parseMediaType(contentType))
// .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" +
// encodedFileName + "\"")
// .body(image.getFileData());
// } catch (NoSuchElementException e) {
// return new ResponseEntity<>(("Error: " + e.getMessage()).getBytes(),
// HttpStatus.NOT_FOUND);
// } catch (Exception e) {
// e.printStackTrace();
// return new ResponseEntity<>("An error occurred while retrieving the
// image".getBytes(),
// HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// // @PutMapping("/{id}/images/{fileName}")
// // public ResponseEntity<String> updateImage(@PathVariable UUID id,
// // @PathVariable String fileName, @RequestParam("file") MultipartFile file) {
// // if (file.isEmpty()) {
// // return new ResponseEntity<>("Please select an image to upload",
// // HttpStatus.BAD_REQUEST);
// // }

// // try {
// // byte[] bytes = file.getBytes();

// // Candidate candidate = candidateRepository.findById(id)
// // .orElseThrow(() -> new NoSuchElementException("Candidate not found"));

// // List<Image> images = candidate.getImages();
// // boolean imageExists = false;

// // for (Image image : images) {
// // if (image.getFileName().equals(fileName)) {
// // image.setFileData(bytes);
// // imageExists = true;
// // break;
// // }
// // }

// // if (!imageExists) {
// // candidate.getImages().add(new Image(fileName, bytes));
// // }

// // candidateRepository.save(candidate);

// // return new ResponseEntity<>("Image updated successfully", HttpStatus.OK);
// // } catch (IOException e) {
// // e.printStackTrace();
// // return new ResponseEntity<>("An error occurred while updating the image",
// // HttpStatus.INTERNAL_SERVER_ERROR);
// // }
// // }
// @PutMapping("/{id}/images/{fileName}")
// public ResponseEntity<String> updateImage(@PathVariable UUID id,
// @PathVariable String fileName,
// @RequestParam("file") MultipartFile file) {
// if (file.isEmpty()) {
// return new ResponseEntity<>("Please select an image to upload",
// HttpStatus.BAD_REQUEST);
// }

// try {
// byte[] bytes = file.getBytes();
// String newFileName = file.getOriginalFilename();

// Candidate userProfile = candidateRepository.findById(id)
// .orElseThrow(() -> new NoSuchElementException("User profile not found"));

// Image imageToUpdate = userProfile.getImages().stream()
// .filter(image -> image.getFileName().equals(fileName))
// .findFirst()
// .orElseThrow(() -> new NoSuchElementException("Image not found"));

// imageToUpdate.setFileName(newFileName);
// imageToUpdate.setFileData(bytes);
// candidateRepository.save(userProfile);

// return new ResponseEntity<>("Image updated successfully", HttpStatus.OK);
// } catch (IOException e) {
// e.printStackTrace();
// return new ResponseEntity<>("An error occurred while updating the image",
// HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// @DeleteMapping("/{id}/images/{fileName}")
// public ResponseEntity<String> deleteImage(@PathVariable UUID id,
// @PathVariable String fileName) {
// try {
// Candidate userProfile = candidateRepository.findById(id)
// .orElseThrow(() -> new NoSuchElementException("User profile not found"));

// boolean removed = userProfile.getImages().removeIf(image ->
// image.getFileName().equals(fileName));

// if (!removed) {
// throw new NoSuchElementException("Image not found");
// }

// candidateRepository.save(userProfile);

// return new ResponseEntity<>("Image deleted successfully", HttpStatus.OK);
// } catch (NoSuchElementException e) {

// return new ResponseEntity<>(("Error HttpStatus.NOT_FOUND: " +
// e.getMessage()), HttpStatus.NOT_FOUND);
// } catch (Exception e) {
// e.printStackTrace();

// return new ResponseEntity<>(("Error HttpStatus.INTERNAL_SERVER_ERROR: " +
// e.getMessage()),
// HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// @DeleteMapping("/{id}/attachments/{fileName}")
// public ResponseEntity<String> deleteAttachment(@PathVariable UUID id,
// @PathVariable String fileName) {
// try {
// Candidate userProfile = candidateRepository.findById(id).orElseThrow();
// List<Attachment> attachments = userProfile.getAttachments();

// // Find the attachment by file name
// Attachment attachmentToDelete = null;
// for (Attachment attachment : attachments) {
// if (attachment.getFileName().equals(fileName)) {
// attachmentToDelete = attachment;
// break;
// }
// }

// // If the attachment was found, remove it
// if (attachmentToDelete != null) {
// attachments.remove(attachmentToDelete);
// candidateRepository.save(userProfile);
// return new ResponseEntity<>("Attachment deleted successfully",
// HttpStatus.OK);
// } else {
// return new ResponseEntity<>("Attachment not found", HttpStatus.NOT_FOUND);
// }
// } catch (Exception e) {
// e.printStackTrace();
// return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// private CandidateDTO convertToDTO(Candidate candidate) {
// CandidateDTO dto = new CandidateDTO();
// dto.setId(candidate.getId());
// dto.setCourtesyTitle(candidate.getCourtesyTitle());
// dto.setCandidateName(candidate.getCandidateName()); // Updated field
// dto.setDateOfBirth(candidate.getDateOfBirth());
// dto.setPosition(candidate.getPosition());
// dto.setSalary(candidate.getSalary());
// dto.setEmploymentType(candidate.getEmploymentType());
// dto.setIndustry(candidate.getIndustry());
// dto.setPhoneNumber(candidate.getPhoneNumber());
// dto.setEmail(candidate.getEmail());
// dto.setEducationLevels(candidate.getEducationLevels());
// dto.setSkills(candidate.getSkills());

// // Convert contact types (handle 'url' if it exists or not)
// List<ContactTypeDTO> contactTypeDTOs = candidate.getContactTypes().stream()
// .map(contactType -> new ContactTypeDTO(contactType.getType(),
// contactType.getUrl())) // Make sure 'url'
// // exists in
// // ContactType
// .collect(Collectors.toList());
// dto.setContactTypes(contactTypeDTOs);

// // Convert attachments
// List<AttachmentDTO> attachmentDTOs = candidate.getAttachments().stream()
// .map(attachment -> new AttachmentDTO(attachment.getFileName(),
// "/api/candidates/" + candidate.getId() + "/attachments/" +
// attachment.getFileName()))
// .collect(Collectors.toList());
// dto.setAttachments(attachmentDTOs);

// // Convert images
// List<ImageDTO> imageDTOs = candidate.getImages().stream()
// .map(image -> new ImageDTO(image.getFileName(),
// "/api/candidates/" + candidate.getId() + "/images/" + image.getFileName()))
// .collect(Collectors.toList());
// dto.setImages(imageDTOs);

// return dto;
// }

// }

// ! End
// package com.example.matching.controller;

// import com.example.matching.dto.AttachmentDTO;
// import com.example.matching.dto.ContactTypeDTO;
// import com.example.matching.dto.ImageDTO;
// import com.example.matching.dto.CandidateDTO;
// import com.example.matching.model.ContactType;
// import com.example.matching.model.Candidate;
// import com.example.matching.model.Candidate.Attachment;
// import com.example.matching.repository.CandidateRepository;
// import com.example.matching.service.CandidateService;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.util.UriUtils;

// import java.io.IOException;
// import java.util.List;
// import java.util.UUID;
// import java.util.Optional;
// import java.util.stream.Collectors;
// import java.util.NoSuchElementException;
// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.Map;

// import com.example.matching.model.Candidate.Image;

// @RestController
// @RequestMapping("/api/candidates")
// @CrossOrigin(origins = "http://localhost:5173")
// public class CandidateController {

// @Autowired
// private CandidateRepository candidateRepository;

// @Autowired
// private CandidateService candidateService;

// // @GetMapping("/search")
// // public ResponseEntity<Object> searchCandidates(@RequestParam String query)
// {
// // try {
// // // Split the query on commas or "and", trim spaces
// // List<String> searchTerms =
// // Arrays.stream(query.split("\\s*,\\s*|\\s+and\\s+"))
// // .map(String::trim)
// // .collect(Collectors.toList());
// // // Use the search terms to find matching candidates
// // List<Candidate> matchingCandidates =
// // candidateService.searchCandidates(searchTerms);
// // return new ResponseEntity<>(matchingCandidates, HttpStatus.OK);
// // } catch (Exception e) {
// // Map<String, Object> errorResponse = new HashMap<>();
// // errorResponse.put("error", "Failed to search candidates: " +
// e.getMessage());
// // return
// //
// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
// // }
// // }
// // @GetMapping("/search")
// // public ResponseEntity<Object> searchCandidates(@RequestParam String query)
// {
// // try {
// // // Split the query by commas, "and", or spaces
// // List<String> searchTerms = Arrays.stream(query.split("[,\\s+and\\s+]"))
// // .map(String::trim)
// // .filter(term -> !term.isEmpty())
// // .collect(Collectors.toList());

// // // Use the search terms to find matching candidates
// // List<Candidate> matchingCandidates =
// // candidateService.searchCandidates(searchTerms);
// // return new ResponseEntity<>(matchingCandidates, HttpStatus.OK);
// // } catch (Exception e) {
// // Map<String, Object> errorResponse = new HashMap<>();
// // errorResponse.put("error", "Failed to search candidates: " +
// e.getMessage());
// // return
// //
// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
// // }
// // }
// // @GetMapping("/search")
// // public ResponseEntity<Object> searchCandidates(@RequestParam String query)
// {
// // try {
// // List<String> searchTerms = Arrays.stream(query.split("[,\\s+and\\s+]"))
// // .map(String::trim)
// // .filter(term -> !term.isEmpty())
// // .collect(Collectors.toList());

// // List<Candidate> matchingCandidates =
// // candidateService.searchCandidates(searchTerms);
// // return new ResponseEntity<>(matchingCandidates, HttpStatus.OK);
// // } catch (Exception e) {
// // Map<String, Object> errorResponse = new HashMap<>();
// // errorResponse.put("error", "Failed to search candidates: " +
// e.getMessage());
// // return
// //
// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
// // }
// // }
// // @GetMapping("/search")
// // public ResponseEntity<Object> searchCandidates(@RequestParam String query)
// {
// // try {
// // // Split the query and convert to lowercase
// // List<String> searchTerms = Arrays.stream(query.split("[,\\s+and\\s+]"))
// // .map(String::trim)
// // .map(String::toLowerCase) // Convert to lowercase
// // .filter(term -> !term.isEmpty())
// // .collect(Collectors.toList());

// // // Use the search terms to find matching candidates
// // List<Candidate> matchingCandidates =
// // candidateService.searchCandidates(searchTerms);
// // return new ResponseEntity<>(matchingCandidates, HttpStatus.OK);
// // } catch (Exception e) {
// // Map<String, Object> errorResponse = new HashMap<>();
// // errorResponse.put("error", "Failed to search candidates: " +
// e.getMessage());
// // return
// //
// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
// // }
// // }
// @GetMapping("/search")
// public ResponseEntity<Object> searchCandidates(@RequestParam String query) {
// try {
// List<String> searchTerms = Arrays.stream(query.split("[,\\s+and\\s+]"))
// .map(String::trim)
// .filter(term -> !term.isEmpty())
// .collect(Collectors.toList());

// List<Candidate> matchingCandidates =
// candidateService.searchCandidates(searchTerms);
// return new ResponseEntity<>(matchingCandidates, HttpStatus.OK);
// } catch (Exception e) {
// Map<String, Object> errorResponse = new HashMap<>();
// errorResponse.put("error", "Failed to search candidates: " + e.getMessage());
// return
// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
// }
// }

// @PostMapping
// public ResponseEntity<Candidate> createCandidate(@RequestBody Candidate
// candidate) {
// Candidate savedCandidate = candidateRepository.save(candidate);
// return new ResponseEntity<>(savedCandidate, HttpStatus.CREATED);
// }

// @PostMapping("/{id}/upload")
// public ResponseEntity<String> uploadFile(@PathVariable UUID id,
// @RequestParam("file") MultipartFile file) {
// if (file.isEmpty()) {
// return new ResponseEntity<>("Please select a file to upload",
// HttpStatus.BAD_REQUEST);
// }

// try {
// byte[] bytes = file.getBytes();
// String fileName = file.getOriginalFilename();

// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new NoSuchElementException("Candidate not found"));
// candidate.getAttachments().add(new Attachment(fileName, bytes));
// candidateRepository.save(candidate);

// return new ResponseEntity<>("You successfully uploaded '" + fileName + "'",
// HttpStatus.OK);
// } catch (IOException e) {
// e.printStackTrace();
// return new ResponseEntity<>("An error occurred while uploading the file",
// HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// @PostMapping("/{id}/upload-image")
// public ResponseEntity<String> uploadImage(@PathVariable UUID id,
// @RequestParam("file") MultipartFile file) {
// if (file.isEmpty()) {
// return new ResponseEntity<>("Please select an image to upload",
// HttpStatus.BAD_REQUEST);
// }

// try {
// byte[] bytes = file.getBytes();
// String fileName = file.getOriginalFilename();

// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new NoSuchElementException("Candidate not found"));
// candidate.getImages().add(new Image(fileName, bytes));
// candidateRepository.save(candidate);

// return new ResponseEntity<>("You successfully uploaded '" + fileName + "'",
// HttpStatus.OK);
// } catch (IOException e) {
// e.printStackTrace();
// return new ResponseEntity<>("An error occurred while uploading the image",
// HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// @GetMapping
// public ResponseEntity<List<CandidateDTO>> getAllCandidates() {
// List<Candidate> candidates =
// candidateRepository.findAllByOrderByUpdatedAtDesc();
// List<CandidateDTO> candidateDTOs =
// candidates.stream().map(this::convertToDTO).collect(Collectors.toList());
// return new ResponseEntity<>(candidateDTOs, HttpStatus.OK);
// }

// @GetMapping("/{id}")
// public ResponseEntity<CandidateDTO> getCandidateById(@PathVariable UUID id) {
// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new NoSuchElementException("Candidate not found"));
// CandidateDTO candidateDTO = convertToDTO(candidate);
// return new ResponseEntity<>(candidateDTO, HttpStatus.OK);
// }

// @PutMapping("/{id}")
// public ResponseEntity<Candidate> updateCandidate(@PathVariable UUID id,
// @RequestBody Candidate candidateDetails) {
// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new NoSuchElementException("Candidate not found"));

// candidate.setCourtesyTitle(candidateDetails.getCourtesyTitle());
// candidate.setCandidateName(candidateDetails.getCandidateName());
// // candidate.setUsername(candidateDetails.getUsername());
// candidate.setDateOfBirth(candidateDetails.getDateOfBirth());
// candidate.setPosition(candidateDetails.getPosition());
// candidate.setSalary(candidateDetails.getSalary());
// candidate.setEmploymentType(candidateDetails.getEmploymentType());
// candidate.setIndustry(candidateDetails.getIndustry());
// candidate.setPhoneNumber(candidateDetails.getPhoneNumber());
// candidate.setEmail(candidateDetails.getEmail());
// candidate.setEducationLevels(candidateDetails.getEducationLevels());
// candidate.setSkills(candidateDetails.getSkills());
// candidate.setContactTypes(candidateDetails.getContactTypes());
// candidate.setAttachments(candidateDetails.getAttachments());

// Candidate updatedCandidate = candidateRepository.save(candidate);
// return new ResponseEntity<>(updatedCandidate, HttpStatus.OK);
// }

// @DeleteMapping("/{id}")
// public ResponseEntity<String> deleteCandidate(@PathVariable UUID id) {
// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new NoSuchElementException("Candidate not found"));
// candidateRepository.delete(candidate);
// return new ResponseEntity<>(HttpStatus.NO_CONTENT);
// }

// @PutMapping("/{id}/update-attachment")
// public ResponseEntity<String> updateAttachment(@PathVariable UUID id,
// @RequestParam("file") MultipartFile file) {
// if (file.isEmpty()) {
// return new ResponseEntity<>("Please select a file to upload",
// HttpStatus.BAD_REQUEST);
// }

// try {
// byte[] bytes = file.getBytes();
// String fileName = file.getOriginalFilename();

// // Find the candidate and update the attachment
// Candidate candidate = candidateRepository.findById(id).orElseThrow();
// List<Attachment> attachments = candidate.getAttachments();
// boolean attachmentExists = false;

// for (Attachment attachment : attachments) {
// if (attachment.getFileName().equals(fileName)) {
// attachment.setFileData(bytes);
// attachmentExists = true;
// break;
// }
// }

// if (!attachmentExists) {
// candidate.getAttachments().add(new Attachment(fileName, bytes));
// }

// candidateRepository.save(candidate);

// return new ResponseEntity<>("Attachment updated successfully",
// HttpStatus.OK);
// } catch (IOException e) {
// e.printStackTrace();
// return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// @GetMapping("/{id}/attachments/{fileName}")
// public ResponseEntity<byte[]> downloadAttachment(@PathVariable UUID id,
// @PathVariable String fileName) {
// try {
// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new NoSuchElementException("Candidate not found"));

// Attachment attachmentToDownload = candidate.getAttachments().stream()
// .filter(attachment -> attachment.getFileName().equals(fileName))
// .findFirst()
// .orElseThrow(() -> new NoSuchElementException("Attachment not found"));

// return ResponseEntity.ok()
// .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
// .body(attachmentToDownload.getFileData());
// } catch (NoSuchElementException e) {
// return new ResponseEntity<>(("Error: " + e.getMessage()).getBytes(),
// HttpStatus.NOT_FOUND);
// } catch (Exception e) {
// e.printStackTrace();
// return new ResponseEntity<>("An error occurred while downloading the
// file".getBytes(),
// HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// @GetMapping("/{id}/images/{fileName}")
// public ResponseEntity<byte[]> viewImage(@PathVariable UUID id, @PathVariable
// String fileName) {
// try {
// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new NoSuchElementException("Candidate not found"));

// // Find the image
// Image image = candidate.getImages().stream()
// .filter(images -> images.getFileName().equals(fileName))
// .findFirst()
// .orElseThrow(() -> new NoSuchElementException("Image not found"));

// // Determine content type based on file extension
// String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
// if (fileName.endsWith(".png")) {
// contentType = MediaType.IMAGE_PNG_VALUE;
// } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
// contentType = MediaType.IMAGE_JPEG_VALUE;
// } else if (fileName.endsWith(".gif")) {
// contentType = MediaType.IMAGE_GIF_VALUE;
// }

// // Encode the filename to handle special characters
// String encodedFileName = UriUtils.encodePathSegment(fileName, "UTF-8");

// return ResponseEntity.ok()
// .contentType(MediaType.parseMediaType(contentType))
// .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" +
// encodedFileName + "\"")
// .body(image.getFileData());
// } catch (NoSuchElementException e) {
// return new ResponseEntity<>(("Error: " + e.getMessage()).getBytes(),
// HttpStatus.NOT_FOUND);
// } catch (Exception e) {
// e.printStackTrace();
// return new ResponseEntity<>("An error occurred while retrieving the
// image".getBytes(),
// HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// @PutMapping("/{id}/images/{fileName}")
// public ResponseEntity<String> updateImage(@PathVariable UUID id,
// @PathVariable String fileName, @RequestParam("file") MultipartFile file) {
// if (file.isEmpty()) {
// return new ResponseEntity<>("Please select an image to upload",
// HttpStatus.BAD_REQUEST);
// }

// try {
// byte[] bytes = file.getBytes();

// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new NoSuchElementException("Candidate not found"));

// List<Image> images = candidate.getImages();
// boolean imageExists = false;

// for (Image image : images) {
// if (image.getFileName().equals(fileName)) {
// image.setFileData(bytes);
// imageExists = true;
// break;
// }
// }

// if (!imageExists) {
// candidate.getImages().add(new Image(fileName, bytes));
// }

// candidateRepository.save(candidate);

// return new ResponseEntity<>("Image updated successfully", HttpStatus.OK);
// } catch (IOException e) {
// e.printStackTrace();
// return new ResponseEntity<>("An error occurred while updating the image",
// HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// private CandidateDTO convertToDTO(Candidate candidate) {
// CandidateDTO dto = new CandidateDTO();
// dto.setId(candidate.getId());
// dto.setCourtesyTitle(candidate.getCourtesyTitle());
// dto.setCandidateName(candidate.getCandidateName()); // Updated field
// dto.setDateOfBirth(candidate.getDateOfBirth());
// dto.setPosition(candidate.getPosition());
// dto.setSalary(candidate.getSalary());
// dto.setEmploymentType(candidate.getEmploymentType());
// dto.setIndustry(candidate.getIndustry());
// dto.setPhoneNumber(candidate.getPhoneNumber());
// dto.setEmail(candidate.getEmail());
// dto.setEducationLevels(candidate.getEducationLevels());
// dto.setSkills(candidate.getSkills());

// // Convert contact types (handle 'url' if it exists or not)
// List<ContactTypeDTO> contactTypeDTOs = candidate.getContactTypes().stream()
// .map(contactType -> new ContactTypeDTO(contactType.getType(),
// contactType.getUrl())) // Make sure 'url'
// // exists in
// // ContactType
// .collect(Collectors.toList());
// dto.setContactTypes(contactTypeDTOs);

// // Convert attachments
// List<AttachmentDTO> attachmentDTOs = candidate.getAttachments().stream()
// .map(attachment -> new AttachmentDTO(attachment.getFileName(),
// "/api/candidates/" + candidate.getId() + "/attachments/" +
// attachment.getFileName()))
// .collect(Collectors.toList());
// dto.setAttachments(attachmentDTOs);

// // Convert images
// List<ImageDTO> imageDTOs = candidate.getImages().stream()
// .map(image -> new ImageDTO(image.getFileName(),
// "/api/candidates/" + candidate.getId() + "/images/" + image.getFileName()))
// .collect(Collectors.toList());
// dto.setImages(imageDTOs);

// return dto;
// }

// }

// ! Below code is good but it still Lond id
// package com.example.matching.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// import com.example.matching.model.Candidate;
// import com.example.matching.service.CandidateService;

// import java.io.IOException;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;
// import java.util.stream.Collectors;

// @RestController
// @RequestMapping("/candidates")
// public class CandidateController {

// @Autowired
// private CandidateService candidateService;

// // @GetMapping("/search")
// // public ResponseEntity<List<Candidate>> searchCandidates(@RequestParam
// String
// // query) {
// // List<Candidate> matchingCandidates =
// // candidateService.searchCandidates(query);
// // return new ResponseEntity<>(matchingCandidates, HttpStatus.OK);
// // }
// @GetMapping("/search")
// public ResponseEntity<List<Candidate>> searchCandidates(@RequestParam String
// query) {
// // Split the input query string into individual search terms based on "," or
// // "and"
// List<String> searchTerms =
// Arrays.stream(query.split("\\s*,\\s*|\\s+and\\s+"))
// .map(String::trim)
// .collect(Collectors.toList());

// // Search for vacancies based on the search terms
// List<Candidate> matchingCandidates =
// candidateService.searchCandidates(searchTerms);

// return new ResponseEntity<>(matchingCandidates, HttpStatus.OK);
// }

// @GetMapping
// public ResponseEntity<List<Candidate>> getAllCandidates() {
// List<Candidate> candidates = candidateService.getAllCandidates();
// return new ResponseEntity<>(candidates, HttpStatus.OK);
// }

// @GetMapping("/{id}")
// public ResponseEntity<Candidate> getCandidateById(@PathVariable Long id) {
// Optional<Candidate> candidate = candidateService.getCandidateById(id);
// return candidate.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
// .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
// }

// @PostMapping
// public ResponseEntity<Candidate> createCandidate(
// @RequestParam("name") String name,
// @RequestParam("position") String position,
// @RequestParam("skills") String skills,
// @RequestParam("resume") MultipartFile resumeFile) {

// try {
// byte[] resume = resumeFile.getBytes();
// Candidate createdCandidate = candidateService.createCandidate(name, position,
// skills, resume);
// return new ResponseEntity<>(createdCandidate, HttpStatus.CREATED);
// } catch (IOException e) {
// return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

// @PutMapping("/{id}")
// public ResponseEntity<Candidate> updateCandidate(
// @PathVariable Long id,
// @RequestParam("name") String name,
// @RequestParam("position") String position,
// @RequestParam("skills") String skills,
// @RequestParam(value = "resume", required = false) MultipartFile resumeFile) {

// try {
// byte[] resume = (resumeFile != null) ? resumeFile.getBytes() : null;
// Candidate updatedCandidate = candidateService.updateCandidate(id, name,
// position, skills, resume);
// return ResponseEntity.ok(updatedCandidate);
// } catch (IOException e) {
// return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
// } catch (IllegalArgumentException e) {
// return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
// }
// }

// @DeleteMapping("/{id}")
// public ResponseEntity<String> deleteCandidate(@PathVariable Long id) {
// candidateService.deleteCandidate(id);
// return ResponseEntity.ok("Candidate deleted successfully");
// }
// }

// ! Below code is good but not include upload CvResume
// package com.example.matching.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.example.matching.model.Candidate;
// import com.example.matching.model.Vacancy;
// import com.example.matching.service.CandidateService;

// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;
// import java.util.stream.Collectors;

// @RestController
// @RequestMapping("/candidates")
// public class CandidateController {
// @Autowired
// private CandidateService candidateService;

// // @GetMapping("/search")
// // public ResponseEntity<List<Candidate>>
// // searchCandidates(@RequestParam(required = false) String name,
// // @RequestParam(required = false) String skills) {
// // List<Candidate> matchingCandidates =
// candidateService.searchCandidates(name,
// // skills);
// // return new ResponseEntity<>(matchingCandidates, HttpStatus.OK);
// // }
// @GetMapping("/search")
// public ResponseEntity<List<Candidate>> searchCandidates(@RequestParam String
// query) {
// // Split the input query string into individual search terms based on "," or
// // "and"
// List<String> searchTerms =
// Arrays.stream(query.split("\\s*,\\s*|\\s+and\\s+"))
// .map(String::trim)
// .collect(Collectors.toList());

// // Search for vacancies based on the search terms
// List<Candidate> matchingCandidates =
// candidateService.searchCandidates(searchTerms);

// return new ResponseEntity<>(matchingCandidates, HttpStatus.OK);
// }

// @GetMapping
// public ResponseEntity<List<Candidate>> getAllCandidates() {
// List<Candidate> candidates = candidateService.getAllCandidates();
// return new ResponseEntity<>(candidates, HttpStatus.OK);
// }

// @GetMapping("/{id}")
// public ResponseEntity<Candidate> getCandidateById(@PathVariable Long id) {
// Optional<Candidate> candidate = candidateService.getCandidateById(id);
// return candidate.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
// .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
// }

// @PostMapping
// public ResponseEntity<Candidate> createCandidate(@RequestBody Candidate
// candidate) {
// Candidate createdCandidate = candidateService.createCandidate(candidate);
// return new ResponseEntity<>(createdCandidate, HttpStatus.CREATED);
// }

// @PutMapping("/{id}")
// public ResponseEntity<Object> updateCandidate(@PathVariable Long id,
// @RequestBody Candidate candidate) {
// try {
// Candidate updatedCandidate = candidateService.updateCandidate(id, candidate);
// return ResponseEntity.ok(updatedCandidate);
// } catch (IllegalArgumentException e) {
// String errorMessage = e.getMessage();
// Map<String, String> errorResponse = new HashMap<>();
// errorResponse.put("error", errorMessage);
// return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
// }
// }
// // @PutMapping("/{id}")
// // public ResponseEntity<Candidate> updateCandidate(@PathVariable Long id,
// // @RequestBody Candidate candidate) {
// // Candidate updatedCandidate = candidateService.updateCandidate(id,
// candidate);
// // return new ResponseEntity<>(updatedCandidate, HttpStatus.OK);
// // }

// // @DeleteMapping("/{id}")
// // public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
// // candidateService.deleteCandidate(id);
// // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
// // }
// @DeleteMapping("/{id}")
// public ResponseEntity<Object> deleteCandidate(@PathVariable Long id) {
// try {
// candidateService.deleteCandidate(id);
// return ResponseEntity.ok("Successfully Deleted");
// } catch (IllegalArgumentException e) {
// // TODO Auto-generated catch block
// String errorMessage = e.getMessage();
// Map<String, String> errorResponse = new HashMap<>();
// errorResponse.put("error", errorMessage);
// return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
// }
// }
// }
