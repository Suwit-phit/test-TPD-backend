package com.example.matching.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import com.example.matching.model.Candidate;
import com.example.matching.model.Candidate.Attachment;
import com.example.matching.model.ContactType;
import com.example.matching.model.Vacancy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExcelGeneratorService {

    //! Not include attachments and image
    public ByteArrayInputStream generateCandidatesExcel(List<Candidate> candidates) throws IOException {
        String[] columns = { "ID", "Courtesy Title", "Candidate Name", "Date of Birth", "Position", "Salary",
                "Employment Type", "Industry", "Phone Number", "Email", "Education Levels", "Skills",
                "Contact Types", "Created At", "Updated At" };
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
    
        Sheet sheet = workbook.createSheet("Candidates");
    
        // Create a row for headers
        Row headerRow = sheet.createRow(0);
    
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }
    
        // Create rows for candidate data
        int rowNum = 1;
        for (Candidate candidate : candidates) {
            Row row = sheet.createRow(rowNum++);
    
            // Set ID (UUID as String)
            row.createCell(0).setCellValue(candidate.getId().toString());
    
            // Set Courtesy Title
            row.createCell(1).setCellValue(candidate.getCourtesyTitle() != null ? candidate.getCourtesyTitle() : "N/A");
    
            // Set Candidate Name
            row.createCell(2).setCellValue(candidate.getCandidateName());
    
            // Set Date of Birth (as String)
            row.createCell(3).setCellValue(candidate.getDateOfBirth() != null ? candidate.getDateOfBirth() : "N/A");
    
            // Set Position
            row.createCell(4).setCellValue(candidate.getPosition());
    
            // Set Salary
            row.createCell(5).setCellValue(candidate.getSalary());
    
            // Set Employment Type
            row.createCell(6).setCellValue(candidate.getEmploymentType());
    
            // Set Industry
            row.createCell(7).setCellValue(candidate.getIndustry());
    
            // Set Phone Number
            row.createCell(8).setCellValue(candidate.getPhoneNumber() != null ? candidate.getPhoneNumber() : "N/A");
    
            // Set Email
            row.createCell(9).setCellValue(candidate.getEmail() != null ? candidate.getEmail() : "N/A");
    
            // Set Education Levels (List<String>)
            row.createCell(10).setCellValue(String.join(", ", candidate.getEducationLevels()));
    
            // Set Skills (List<String>)
            row.createCell(11).setCellValue(String.join(", ", candidate.getSkills()));
    
            // Set Contact Types (List<Candidate.ContactType>)
            row.createCell(12).setCellValue(candidate.getContactTypes() != null
                    ? candidate.getContactTypes().stream().map(ContactType::toString)
                            .collect(Collectors.joining(", "))
                    : "N/A");
            // row.createCell(12).setCellValue(candidate.getContactTypes() != null
            //         ? candidate.getContactTypes().stream().map(Candidate.ContactType::toString)
            //                 .collect(Collectors.joining(", "))
            //         : "N/A");
    
            // Set Created At
            row.createCell(13).setCellValue(candidate.getCreatedAt() != null ? candidate.getCreatedAt().toString() : "N/A");
    
            // Set Updated At
            row.createCell(14).setCellValue(candidate.getUpdatedAt() != null ? candidate.getUpdatedAt().toString() : "N/A");
        }
    
        workbook.write(out);
        workbook.close();
    
        return new ByteArrayInputStream(out.toByteArray());
    }
    
    
    //! Include every thing
    // public ByteArrayInputStream generateCandidatesExcel(List<Candidate> candidates) throws IOException {
    //     String[] columns = { "ID", "Courtesy Title", "Candidate Name", "Date of Birth", "Position", "Salary",
    //             "Employment Type", "Industry", "Phone Number", "Email", "Education Levels", "Skills",
    //             "Contact Types", "Attachments", "Images", "Created At", "Updated At" };
    //     Workbook workbook = new XSSFWorkbook();
    //     ByteArrayOutputStream out = new ByteArrayOutputStream();

    //     Sheet sheet = workbook.createSheet("Candidates");

    //     // Create a row for headers
    //     Row headerRow = sheet.createRow(0);

    //     for (int i = 0; i < columns.length; i++) {
    //         Cell cell = headerRow.createCell(i);
    //         cell.setCellValue(columns[i]);
    //     }

    //     // Create rows for candidate data
    //     int rowNum = 1;
    //     for (Candidate candidate : candidates) {
    //         Row row = sheet.createRow(rowNum++);

    //         // Set ID (UUID as String)
    //         row.createCell(0).setCellValue(candidate.getId().toString());

    //         // Set Courtesy Title
    //         row.createCell(1).setCellValue(candidate.getCourtesyTitle() != null ? candidate.getCourtesyTitle() : "N/A");

    //         // Set Candidate Name
    //         row.createCell(2).setCellValue(candidate.getCandidateName());

    //         // Set Date of Birth (as String)
    //         row.createCell(3).setCellValue(candidate.getDateOfBirth() != null ? candidate.getDateOfBirth() : "N/A");

    //         // Set Position
    //         row.createCell(4).setCellValue(candidate.getPosition());

    //         // Set Salary
    //         row.createCell(5).setCellValue(candidate.getSalary());

    //         // Set Employment Type
    //         row.createCell(6).setCellValue(candidate.getEmploymentType());

    //         // Set Industry
    //         row.createCell(7).setCellValue(candidate.getIndustry());

    //         // Set Phone Number
    //         row.createCell(8).setCellValue(candidate.getPhoneNumber() != null ? candidate.getPhoneNumber() : "N/A");

    //         // Set Email
    //         row.createCell(9).setCellValue(candidate.getEmail() != null ? candidate.getEmail() : "N/A");

    //         // Set Education Levels (List<String>)
    //         row.createCell(10).setCellValue(String.join(", ", candidate.getEducationLevels()));

    //         // Set Skills (List<String>)
    //         row.createCell(11).setCellValue(String.join(", ", candidate.getSkills()));

    //         // Set Contact Types (List<Candidate.ContactType>)
    //         row.createCell(12).setCellValue(candidate.getContactTypes() != null
    //                 ? candidate.getContactTypes().stream().map(Candidate.ContactType::toString)
    //                         .collect(Collectors.joining(", "))
    //                 : "N/A");

    //         // Set Attachments (List<Attachment>)
    //         row.createCell(13).setCellValue(candidate.getAttachments() != null
    //                 ? candidate.getAttachments().stream().map(Attachment::toString).collect(Collectors.joining(", "))
    //                 : "N/A");

    //         // Set Images (List<Candidate.Image>)
    //         row.createCell(14).setCellValue(candidate.getImages() != null
    //                 ? candidate.getImages().stream().map(Candidate.Image::toString).collect(Collectors.joining(", "))
    //                 : "N/A");

    //         // Set Created At
    //         row.createCell(15)
    //                 .setCellValue(candidate.getCreatedAt() != null ? candidate.getCreatedAt().toString() : "N/A");

    //         // Set Updated At
    //         row.createCell(16)
    //                 .setCellValue(candidate.getUpdatedAt() != null ? candidate.getUpdatedAt().toString() : "N/A");
    //     }

    //     workbook.write(out);
    //     workbook.close();

    //     return new ByteArrayInputStream(out.toByteArray());
    // }

    public ByteArrayInputStream generateVacanciesExcel(List<Vacancy> vacancies) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Vacancies");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = { "ID", "Position Title", "Position Applied", "Skills", "Salary", "Currency Code",
                    "Date of Application", "Created At", "Updated At" };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderCellStyle(workbook));
            }

            // Fill in data
            int rowNum = 1;
            for (Vacancy vacancy : vacancies) {
                Row row = sheet.createRow(rowNum++);

                // Handle UUID as string
                row.createCell(0).setCellValue(vacancy.getId().toString());

                // Use the correct getter methods for the fields
                row.createCell(1).setCellValue(vacancy.getPositionTitle());
                row.createCell(2).setCellValue(vacancy.getPositionApplied());

                // Handle skills (List<String>) by joining them into a single string
                row.createCell(3).setCellValue(vacancy.getSkills());

                // Handle salary and currency code
                row.createCell(4).setCellValue(vacancy.getSalary() != null ? vacancy.getSalary() : 0.0);
                row.createCell(5).setCellValue(vacancy.getCurrencyCode() != null ? vacancy.getCurrencyCode() : "N/A");

                // Handle date of application (LocalDate)
                row.createCell(6).setCellValue(
                        vacancy.getDateOfApplication() != null ? vacancy.getDateOfApplication().toString() : "N/A");

                // Handle timestamps for createdAt and updatedAt
                row.createCell(7).setCellValue(vacancy.getCreatedAt() != null ? vacancy.getCreatedAt().toString() : "");
                row.createCell(8).setCellValue(vacancy.getUpdatedAt() != null ? vacancy.getUpdatedAt().toString() : "");
            }

            // Auto-size all columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to ByteArrayOutputStream
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}

// ! Below code is good but it doesn't include every that I have set in the
// "model Vacancy"
// package com.example.matching.service;

// import org.apache.poi.ss.usermodel.*;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;
// import org.springframework.stereotype.Service;

// import com.example.matching.model.Candidate;
// import com.example.matching.model.Vacancy;

// import java.io.ByteArrayInputStream;
// import java.io.ByteArrayOutputStream;
// import java.io.IOException;
// import java.util.List;

// @Service
// public class ExcelGeneratorService {

// public ByteArrayInputStream generateCandidatesExcel(List<Candidate>
// candidates) throws IOException {
// String[] columns = { "ID", "Name", "Position", "Skills", "Timestamp" };
// Workbook workbook = new XSSFWorkbook();
// ByteArrayOutputStream out = new ByteArrayOutputStream();

// Sheet sheet = workbook.createSheet("Candidates");

// // Create a row for headers
// Row headerRow = sheet.createRow(0);

// for (int i = 0; i < columns.length; i++) {
// Cell cell = headerRow.createCell(i);
// cell.setCellValue(columns[i]);
// }

// // Create rows for candidate data
// int rowNum = 1;
// for (Candidate candidate : candidates) {
// Row row = sheet.createRow(rowNum++);
// row.createCell(0).setCellValue(candidate.getId());
// row.createCell(1).setCellValue(candidate.getName());
// row.createCell(2).setCellValue(candidate.getPosition());
// row.createCell(3).setCellValue(candidate.getSkills());

// // Handle null timestamp
// if (candidate.getTimestamp() != null) {
// row.createCell(4).setCellValue(candidate.getTimestamp().toString());
// } else {
// row.createCell(4).setCellValue("N/A"); // Handle null timestamp with default
// value
// }
// }

// workbook.write(out);
// workbook.close();

// return new ByteArrayInputStream(out.toByteArray());
// }

// public ByteArrayInputStream generateVacanciesExcel(List<Vacancy> vacancies) {
// try (Workbook workbook = new XSSFWorkbook()) {
// Sheet sheet = workbook.createSheet("Vacancies");

// // Create header row
// Row headerRow = sheet.createRow(0);
// String[] headers = { "ID", "Title", "Position", "Skills", "Created At",
// "Updated At" };

// for (int i = 0; i < headers.length; i++) {
// Cell cell = headerRow.createCell(i);
// cell.setCellValue(headers[i]);
// cell.setCellStyle(createHeaderCellStyle(workbook));
// }

// // Fill in data
// int rowNum = 1;
// for (Vacancy vacancy : vacancies) {
// Row row = sheet.createRow(rowNum++);

// // Convert UUID to String before setting the value
// row.createCell(0).setCellValue(vacancy.getId().toString()); // UUID to String
// // row.createCell(0).setCellValue(vacancy.getId());
// row.createCell(1).setCellValue(vacancy.getTitle());
// row.createCell(2).setCellValue(vacancy.getPosition());
// row.createCell(3).setCellValue(vacancy.getSkills());
// row.createCell(4).setCellValue(vacancy.getCreatedAt() != null ?
// vacancy.getCreatedAt().toString() : "");
// row.createCell(5).setCellValue(vacancy.getUpdatedAt() != null ?
// vacancy.getUpdatedAt().toString() : "");
// }

// // Auto-size all columns
// for (int i = 0; i < headers.length; i++) {
// sheet.autoSizeColumn(i);
// }

// // Write to ByteArrayOutputStream
// ByteArrayOutputStream out = new ByteArrayOutputStream();
// workbook.write(out);
// return new ByteArrayInputStream(out.toByteArray());

// } catch (IOException e) {
// e.printStackTrace();
// return null;
// }
// }

// private CellStyle createHeaderCellStyle(Workbook workbook) {
// CellStyle style = workbook.createCellStyle();
// Font font = workbook.createFont();
// font.setBold(true);
// style.setFont(font);
// return style;
// }
// }

// ! Below code is good but id is still Long
// package com.example.matching.service;

// import org.apache.poi.ss.usermodel.*;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;
// import org.springframework.stereotype.Service;

// import com.example.matching.model.Candidate;
// import com.example.matching.model.Vacancy;

// import java.io.ByteArrayInputStream;
// import java.io.ByteArrayOutputStream;
// import java.io.IOException;
// import java.util.List;

// @Service
// public class ExcelGeneratorService {

// public ByteArrayInputStream generateCandidatesExcel(List<Candidate>
// candidates) throws IOException {
// String[] columns = { "ID", "Name", "Position", "Skills", "Timestamp" };
// Workbook workbook = new XSSFWorkbook();
// ByteArrayOutputStream out = new ByteArrayOutputStream();

// Sheet sheet = workbook.createSheet("Candidates");

// // Create a row for headers
// Row headerRow = sheet.createRow(0);

// for (int i = 0; i < columns.length; i++) {
// Cell cell = headerRow.createCell(i);
// cell.setCellValue(columns[i]);
// }

// // Create rows for candidate data
// int rowNum = 1;
// for (Candidate candidate : candidates) {
// Row row = sheet.createRow(rowNum++);
// row.createCell(0).setCellValue(candidate.getId());
// row.createCell(1).setCellValue(candidate.getName());
// row.createCell(2).setCellValue(candidate.getPosition());
// row.createCell(3).setCellValue(candidate.getSkills());

// // Handle null timestamp
// if (candidate.getTimestamp() != null) {
// row.createCell(4).setCellValue(candidate.getTimestamp().toString());
// } else {
// row.createCell(4).setCellValue("N/A"); // Handle null timestamp with default
// value
// }
// }

// workbook.write(out);
// workbook.close();

// return new ByteArrayInputStream(out.toByteArray());
// }

// // public ByteArrayInputStream generateVacanciesExcel(List<Vacancy>
// vacancies)
// // throws IOException {
// // String[] columns = { "ID", "Title", "Position", "Skills" };
// // Workbook workbook = new XSSFWorkbook();
// // ByteArrayOutputStream out = new ByteArrayOutputStream();

// // Sheet sheet = workbook.createSheet("Vacancies");

// // // Create a row for headers
// // Row headerRow = sheet.createRow(0);

// // for (int i = 0; i < columns.length; i++) {
// // Cell cell = headerRow.createCell(i);
// // cell.setCellValue(columns[i]);
// // }

// // // Create rows for vacancy data
// // int rowNum = 1;
// // for (Vacancy vacancy : vacancies) {
// // Row row = sheet.createRow(rowNum++);
// // row.createCell(0).setCellValue(vacancy.getId());
// // row.createCell(1).setCellValue(vacancy.getTitle());
// // row.createCell(2).setCellValue(vacancy.getPosition());
// // row.createCell(3).setCellValue(vacancy.getSkills());
// // }

// // workbook.write(out);
// // workbook.close();

// // return new ByteArrayInputStream(out.toByteArray());
// // }
// public ByteArrayInputStream generateVacanciesExcel(List<Vacancy> vacancies) {
// try (Workbook workbook = new XSSFWorkbook()) {
// Sheet sheet = workbook.createSheet("Vacancies");

// // Create header row
// Row headerRow = sheet.createRow(0);
// String[] headers = { "ID", "Title", "Position", "Skills", "Created At",
// "Updated At" };

// for (int i = 0; i < headers.length; i++) {
// Cell cell = headerRow.createCell(i);
// cell.setCellValue(headers[i]);
// cell.setCellStyle(createHeaderCellStyle(workbook));
// }

// // Fill in data
// int rowNum = 1;
// for (Vacancy vacancy : vacancies) {
// Row row = sheet.createRow(rowNum++);
// row.createCell(0).setCellValue(vacancy.getId());
// row.createCell(1).setCellValue(vacancy.getTitle());
// row.createCell(2).setCellValue(vacancy.getPosition());
// row.createCell(3).setCellValue(vacancy.getSkills());
// row.createCell(4).setCellValue(vacancy.getCreatedAt() != null ?
// vacancy.getCreatedAt().toString() : "");
// row.createCell(5).setCellValue(vacancy.getUpdatedAt() != null ?
// vacancy.getUpdatedAt().toString() : "");
// }

// // Auto-size all columns
// for (int i = 0; i < headers.length; i++) {
// sheet.autoSizeColumn(i);
// }

// // Write to ByteArrayOutputStream
// ByteArrayOutputStream out = new ByteArrayOutputStream();
// workbook.write(out);
// return new ByteArrayInputStream(out.toByteArray());

// } catch (IOException e) {
// e.printStackTrace();
// return null;
// }
// }

// private CellStyle createHeaderCellStyle(Workbook workbook) {
// CellStyle style = workbook.createCellStyle();
// Font font = workbook.createFont();
// font.setBold(true);
// style.setFont(font);
// return style;
// }
// }
