package com.example.matching.service;

import com.example.matching.model.Candidate;
import com.example.matching.model.Vacancy;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
public class CsvExportService {

    // public void writeCandidatesToCsv(PrintWriter writer, List<Candidate>
    // candidates) {
    // try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
    // .withHeader("ID", "Name", "Position", "Skills", "Timestamp"))) {
    // for (Candidate candidate : candidates) {
    // csvPrinter.printRecord(
    // candidate.getId(),
    // candidate.getName(),
    // candidate.getPosition(),
    // candidate.getSkills(),
    // candidate.getTimestamp());
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    public void writeCandidatesToCsv(PrintWriter writer, List<Candidate> candidates) {
        try (CSVPrinter csvPrinter = new CSVPrinter(writer,
                CSVFormat.Builder.create(CSVFormat.DEFAULT)
                        .setHeader("ID", "Courtesy Title", "Candidate Name", "Date Of Birth", "Position", "Salary",
                                "Employment Type", "Industry", "Phone Number", "Email", "Education Levels", "Skills",
                                "Contact Types", "Attachments", "Images", "Created At", "Updated At")
                        .build())) {

            for (Candidate candidate : candidates) {
                csvPrinter.printRecord(
                        candidate.getId(),
                        candidate.getCourtesyTitle(),
                        candidate.getCandidateName(),
                        candidate.getDateOfBirth(),
                        candidate.getPosition(),
                        candidate.getSalary(),
                        candidate.getEmploymentType(),
                        candidate.getIndustry(),
                        candidate.getPhoneNumber(),
                        candidate.getEmail(),
                        candidate.getEducationLevels() != null ? String.join(", ", candidate.getEducationLevels()) : "",
                        candidate.getSkills() != null ? String.join(", ", candidate.getSkills()) : "",
                        candidate.getContactTypes() != null ? candidate.getContactTypes().toString() : "",
                        candidate.getAttachments() != null ? candidate.getAttachments().toString() : "",
                        candidate.getImages() != null ? candidate.getImages().toString() : "",
                        candidate.getCreatedAt(),
                        candidate.getUpdatedAt());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public void writeVacanciesToCsv(PrintWriter writer, List<Vacancy> vacancies)
    // {
    // try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
    // .withHeader("ID", "Title", "Position", "Skills", "Created At", "Updated
    // At"))) {
    // for (Vacancy vacancy : vacancies) {
    // csvPrinter.printRecord(
    // vacancy.getId(),
    // vacancy.getTitle(),
    // vacancy.getPosition(),
    // vacancy.getSkills(),
    // vacancy.getCreatedAt(),
    // vacancy.getUpdatedAt());
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    // public void writeVacanciesToCsv(PrintWriter writer, List<Vacancy> vacancies) {
    //     try (CSVPrinter csvPrinter = new CSVPrinter(writer,
    //             CSVFormat.Builder.create(CSVFormat.DEFAULT)
    //                     .setHeader("ID", "Title", "Position", "Skills", "Salary", "Currency Code", "Created At",
    //                             "Updated At")
    //                     .build())) {

    //         for (Vacancy vacancy : vacancies) {
    //             csvPrinter.printRecord(
    //                     vacancy.getId(),
    //                     vacancy.getTitle(),
    //                     vacancy.getPosition(),
    //                     vacancy.getSkills(),
    //                     vacancy.getSalary(),
    //                     vacancy.getCurrencyCode(),
    //                     vacancy.getCreatedAt(),
    //                     vacancy.getUpdatedAt());
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
    public void writeVacanciesToCsv(PrintWriter writer, List<Vacancy> vacancies) {
        try (CSVPrinter csvPrinter = new CSVPrinter(writer,
                CSVFormat.Builder.create(CSVFormat.DEFAULT)
                        .setHeader("ID", "Position Title", "Position Applied", "Skills", "Salary", "Currency Code", "Created At",
                                "Updated At")
                        .build())) {
    
            for (Vacancy vacancy : vacancies) {
                csvPrinter.printRecord(
                        vacancy.getId(),
                        vacancy.getPositionTitle(), // Renamed from getTitle() to getPositionTitle()
                        vacancy.getPositionApplied(), // Use getPositionApplied() instead of getPosition()
                        vacancy.getSkills(),
                        vacancy.getSalary(),
                        vacancy.getCurrencyCode(),
                        vacancy.getCreatedAt(),
                        vacancy.getUpdatedAt());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

}
