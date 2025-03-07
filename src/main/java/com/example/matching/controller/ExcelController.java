package com.example.matching.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.matching.model.Candidate;
import com.example.matching.model.Vacancy;
import com.example.matching.repository.CandidateRepository;
import com.example.matching.repository.VacancyRepository;
import com.example.matching.service.ExcelGeneratorService;
import com.example.matching.service.VacancyService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class ExcelController {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private VacancyRepository vacancyRepository;

     @Autowired
    private VacancyService vacancyService; // Assuming you have a VacancyService to fetch data

    @Autowired
    private ExcelGeneratorService excelGeneratorService;

    @GetMapping("/candidates")
    public ResponseEntity<byte[]> downloadCandidatesExcel() throws IOException {
        List<Candidate> candidates = candidateRepository.findAll();
        ByteArrayInputStream in = excelGeneratorService.generateCandidatesExcel(candidates);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=candidates.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }

    //* Below code is good as well */
    // @GetMapping("/vacancies")
    // public ResponseEntity<byte[]> downloadVacanciesExcel() throws IOException {
    //     List<Vacancy> vacancies = vacancyRepository.findAll();
    //     ByteArrayInputStream in = excelGeneratorService.generateVacanciesExcel(vacancies);

    //     HttpHeaders headers = new HttpHeaders();
    //     headers.add("Content-Disposition", "attachment; filename=vacancies.xlsx");

    //     return ResponseEntity.ok()
    //             .headers(headers)
    //             .contentType(MediaType.APPLICATION_OCTET_STREAM)
    //             .body(in.readAllBytes());
    // }
    @GetMapping("/vacancies")
    public ResponseEntity<?> downloadVacanciesExcel() {
        List<Vacancy> vacancies = vacancyService.getAllVacancies(); // Fetch vacancies from DB
        ByteArrayInputStream excelFile = excelGeneratorService.generateVacanciesExcel(vacancies);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=vacancies.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile.readAllBytes());
    }
}
