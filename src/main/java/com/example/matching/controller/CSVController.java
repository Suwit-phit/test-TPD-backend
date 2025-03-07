// package com.example.matching.controller;

// import com.example.matching.model.Candidate;
// import com.example.matching.model.Vacancy;
// import com.example.matching.service.CandidateService;
// import com.example.matching.service.VacancyService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import java.io.ByteArrayOutputStream;
// import java.io.IOException;
// import java.io.OutputStreamWriter;
// import java.nio.charset.StandardCharsets;
// import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;
// import java.util.List;

// @RestController
// @RequestMapping("/api/csv")
// public class CSVController {

//     @Autowired
//     private CandidateService candidateService;

//     @Autowired
//     private VacancyService vacancyService;

//     private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//     @GetMapping("/candidates")
//     public ResponseEntity<byte[]> generateCandidatesCSV() {
//         try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//              OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {

//             List<Candidate> candidates = candidateService.getAllCandidates();

//             writer.write("ID,Name,Email,Phone,DateOfBirth\n");
//             for (Candidate candidate : candidates) {
//                 writer.write(String.format("%s,%s,%s,%s,%s\n",
//                         candidate.getId(),
//                         candidate.getName(),
//                         candidate.getPosition(),
//                         candidate.getSkills(),
//                         candidate.getTimestamp() != null ? candidate.getTimestamp().toString() : ""));
//                         // candidate.getEmail(),
//                         // candidate.getPhone(),
//                         // candidate.getDateOfBirth() != null ? candidate.getDateOfBirth().toString() : ""));
//                         //                         .append(candidate.getPosition()).append(",")
// //                         .append(candidate.getSkills()).append(",")
// //                         .append(dateFormat.format(candidate.getTimestamp())).append("\n");
//             }

//             writer.flush();

//             HttpHeaders headers = new HttpHeaders();
//             headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=candidates.csv");
//             headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

//             return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
//         } catch (IOException e) {
//             e.printStackTrace();
//             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }

//     @GetMapping("/vacancies")
//     public ResponseEntity<byte[]> generateVacanciesCSV() {
//         try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//              OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {

//             List<Vacancy> vacancies = vacancyService.getAllVacancies();

//             writer.write("ID,Title,Position,Skills,CreatedAt,UpdatedAt\n");
//             for (Vacancy vacancy : vacancies) {
//                 writer.write(String.format("%s,%s,%s,%s,%s,%s\n",
//                         vacancy.getId(),
//                         vacancy.getTitle(),
//                         vacancy.getPosition(),
//                         vacancy.getSkills(),
//                         vacancy.getCreatedAt() != null ? vacancy.getCreatedAt().format(DATE_FORMATTER) : "",
//                         vacancy.getUpdatedAt() != null ? vacancy.getUpdatedAt().format(DATE_FORMATTER) : ""));
//             }

//             writer.flush();

//             HttpHeaders headers = new HttpHeaders();
//             headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vacancies.csv");
//             headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

//             return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
//         } catch (IOException e) {
//             e.printStackTrace();
//             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }
// }


// // package com.example.matching.controller;

// // import com.example.matching.model.Candidate;
// // import com.example.matching.model.Vacancy;
// // import com.example.matching.service.CandidateService;
// // import com.example.matching.service.VacancyService;
// // import org.slf4j.Logger;
// // import org.slf4j.LoggerFactory;
// // import org.springframework.core.io.ByteArrayResource;
// // import org.springframework.http.HttpHeaders;
// // import org.springframework.http.HttpStatus;
// // import org.springframework.http.MediaType;
// // import org.springframework.http.ResponseEntity;
// // import org.springframework.web.bind.annotation.GetMapping;
// // import org.springframework.web.bind.annotation.RequestMapping;
// // import org.springframework.web.bind.annotation.RestController;

// // import java.text.SimpleDateFormat;
// // import java.time.format.DateTimeFormatter;
// // import java.util.List;

// // @RestController
// // @RequestMapping("/api")
// // public class CSVController {

// //     private static final Logger logger = LoggerFactory.getLogger(CSVController.class);

// //     private final CandidateService candidateService;
// //     private final VacancyService vacancyService;

// //     public CSVController(CandidateService candidateService, VacancyService vacancyService) {
// //         this.candidateService = candidateService;
// //         this.vacancyService = vacancyService;
// //     }

// //     @GetMapping("/candidates/csv")
// //     public ResponseEntity<ByteArrayResource> generateCandidatesCSV() {
// //         try {
// //             List<Candidate> candidates = candidateService.getAllCandidates();
// //             StringBuilder csvContent = new StringBuilder("Id,Name,Position,Skills,Timestamp\n");

// //             SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

// //             for (Candidate candidate : candidates) {
// //                 csvContent.append(candidate.getId()).append(",")
// //                         .append(candidate.getName()).append(",")
// //                         .append(candidate.getPosition()).append(",")
// //                         .append(candidate.getSkills()).append(",")
// //                         .append(dateFormat.format(candidate.getTimestamp())).append("\n");
// //             }

// //             ByteArrayResource resource = new ByteArrayResource(csvContent.toString().getBytes());
// //             HttpHeaders headers = new HttpHeaders();
// //             headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=candidates.csv");

// //             return ResponseEntity.ok()
// //                     .headers(headers)
// //                     .contentLength(resource.contentLength())
// //                     .contentType(MediaType.parseMediaType("text/csv"))
// //                     .body(resource);
// //         } catch (Exception e) {
// //             logger.error("Error generating candidates CSV", e);
// //             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
// //         }
// //     }

// //     @GetMapping("/vacancies/csv")
// //     public ResponseEntity<ByteArrayResource> generateVacanciesCSV() {
// //         try {
// //             List<Vacancy> vacancies = vacancyService.getAllVacancies();
// //             StringBuilder csvContent = new StringBuilder("Id,Title,Position,Skills,CreatedAt,UpdatedAt\n");

// //             DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

// //             for (Vacancy vacancy : vacancies) {
// //                 csvContent.append(vacancy.getId()).append(",")
// //                         .append(vacancy.getTitle()).append(",")
// //                         .append(vacancy.getPosition()).append(",")
// //                         .append(vacancy.getSkills()).append(",")
// //                         .append(vacancy.getCreatedAt().format(dateTimeFormatter)).append(",")
// //                         .append(vacancy.getUpdatedAt().format(dateTimeFormatter)).append("\n");
// //             }

// //             ByteArrayResource resource = new ByteArrayResource(csvContent.toString().getBytes());
// //             HttpHeaders headers = new HttpHeaders();
// //             headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vacancies.csv");

// //             return ResponseEntity.ok()
// //                     .headers(headers)
// //                     .contentLength(resource.contentLength())
// //                     .contentType(MediaType.parseMediaType("text/csv"))
// //                     .body(resource);
// //         } catch (Exception e) {
// //             logger.error("Error generating vacancies CSV", e);
// //             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
// //         }
// //     }
// // }
