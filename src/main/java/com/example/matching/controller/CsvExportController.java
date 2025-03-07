package com.example.matching.controller;

import com.example.matching.model.Candidate;
import com.example.matching.model.Vacancy;
import com.example.matching.repository.CandidateRepository;
import com.example.matching.repository.VacancyRepository;
import com.example.matching.service.CsvExportService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class CsvExportController {

    @Autowired
    private CsvExportService csvExportService;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private VacancyRepository vacancyRepository;

    @GetMapping("/export/candidates/csv")
    public void exportCandidatesToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=candidates.csv");
        List<Candidate> candidates = candidateRepository.findAll();
        csvExportService.writeCandidatesToCsv(response.getWriter(), candidates);
    }

    @GetMapping("/export/vacancies/csv")
    public void exportVacanciesToCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; file=vacancies.csv");
        List<Vacancy> vacancies = vacancyRepository.findAll();
        csvExportService.writeVacanciesToCsv(response.getWriter(), vacancies);
    }
}
