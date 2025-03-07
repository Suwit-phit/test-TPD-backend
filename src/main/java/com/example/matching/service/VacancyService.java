package com.example.matching.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.matching.model.Vacancy;
import com.example.matching.repository.VacancyRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

@Service
public class VacancyService {

    @Autowired
    private VacancyRepository vacancyRepository;

    // public List<Vacancy> searchVacancies(List<String> searchTerms) {
    // List<Vacancy> matchingVacancies = new ArrayList<>();
    // for (String term : searchTerms) {
    // List<Vacancy> termVacancies = vacancyRepository
    // .findByPositionTitleContainingIgnoreCaseOrPositionAppliedContainingIgnoreCaseOrSkillsContainingIgnoreCase(term,
    // term, term);
    // matchingVacancies.addAll(termVacancies);
    // // List<Vacancy> termVacancies = vacancyRepository
    // //
    // .findByTitleContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(term,
    // term, term);
    // // matchingVacancies.addAll(termVacancies);
    // }
    // if (matchingVacancies.isEmpty()) {
    // throw new IllegalArgumentException("No vacancies match the given search
    // criteria.");
    // }
    // return matchingVacancies;
    // }
    public List<Vacancy> searchVacancies(List<String> searchTerms) {
        Set<Vacancy> matchingVacancies = new HashSet<>();

        // Loop through each search term
        for (String term : searchTerms) {
            // Search by position title, position applied, or skills
            List<Vacancy> termVacancies = vacancyRepository
                    .findByPositionTitleContainingIgnoreCaseOrPositionAppliedContainingIgnoreCaseOrSkillsContainingIgnoreCase(
                            term, term, term);

            matchingVacancies.addAll(termVacancies); // Add to the result set to avoid duplicates
        }

        // Check if any vacancies were found
        if (matchingVacancies.isEmpty()) {
            throw new IllegalArgumentException("No vacancies match the given search criteria.");
        }

        return new ArrayList<>(matchingVacancies); // Convert Set to List to maintain unique results
    }

    public List<Vacancy> getAllVacancies() {
        List<Vacancy> vacancies = vacancyRepository.findAllByOrderByUpdatedAtDesc();
        if (vacancies.isEmpty()) {
            throw new IllegalStateException("No vacancies available.");
        }
        return vacancies;
    }

    public Optional<Vacancy> getVacancyById(UUID id) {
        return vacancyRepository.findById(id);
    }

    public Vacancy createVacancy(Vacancy vacancy) {
        if (vacancy.getPositionTitle() == null || vacancy.getPositionApplied() == null) {
            throw new IllegalArgumentException("Vacancy title and position cannot be null.");
        }
        // if (vacancy.getTitle() == null || vacancy.getPosition() == null) {
        // throw new IllegalArgumentException("Vacancy title and position cannot be
        // null.");
        // }
        return vacancyRepository.save(vacancy);
    }

    public Vacancy updateVacancy(UUID id, Vacancy vacancy) {
        Optional<Vacancy> existingVacancyOptional = vacancyRepository.findById(id);
        if (existingVacancyOptional.isPresent()) {
            vacancy.setId(id); // Update by UUID
            return vacancyRepository.save(vacancy);
        } else {
            throw new IllegalArgumentException("Vacancy with ID " + id + " does not exist.");
        }
    }

    public void deleteVacancy(UUID id) {
        Optional<Vacancy> existingVacancyOptional = vacancyRepository.findById(id);
        if (existingVacancyOptional.isPresent()) {
            vacancyRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Vacancy with ID " + id + " does not exist.");
        }
    }
}

// ! Below is good but it doesn't try catch error
// package com.example.matching.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.example.matching.model.Vacancy;
// import com.example.matching.repository.VacancyRepository;

// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;
// import java.util.ArrayList;

// @Service
// public class VacancyService {
// @Autowired
// private VacancyRepository vacancyRepository;

// // Search in Vacancy Position and Skills
// public List<Vacancy> searchVacancies(List<String> searchTerms) {
// List<Vacancy> matchingVacancies = new ArrayList<>();

// for (String term : searchTerms) {
// List<Vacancy> termVacancies = vacancyRepository
// .findByTitleContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(term,
// term, term);
// matchingVacancies.addAll(termVacancies);
// }

// return matchingVacancies;
// }

// public List<Vacancy> getAllVacancies() {
// return vacancyRepository.findAllByOrderByUpdatedAtDesc();
// }

// public Optional<Vacancy> getVacancyById(UUID id) {
// return vacancyRepository.findById(id);
// }

// public Vacancy createVacancy(Vacancy vacancy) {
// return vacancyRepository.save(vacancy);
// }

// public Vacancy updateVacancy(UUID id, Vacancy vacancy) {
// Optional<Vacancy> existingVacancyOptional = vacancyRepository.findById(id);
// if (existingVacancyOptional.isPresent()) {
// vacancy.setId(id); // Update by UUID
// return vacancyRepository.save(vacancy);
// } else {
// throw new IllegalArgumentException("Vacancy with ID " + id + " does not
// exist.");
// }
// }

// public void deleteVacancy(UUID id) {
// Optional<Vacancy> existingVacancyOptional = vacancyRepository.findById(id);
// if (existingVacancyOptional.isPresent()) {
// vacancyRepository.deleteById(id);
// } else {
// throw new IllegalArgumentException("Vacancy with ID " + id + " does not
// exist.");
// }
// }
// }

// ! Below code is good but it doesn't include every that I have set in the
// "model Vacancy"
// package com.example.matching.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.example.matching.model.Vacancy;
// import com.example.matching.repository.VacancyRepository;

// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;
// import java.util.ArrayList;

// @Service
// public class VacancyService {
// @Autowired
// private VacancyRepository vacancyRepository;

// // Search in Vacancy Position and Skills
// public List<Vacancy> searchVacancies(List<String> searchTerms) {
// List<Vacancy> matchingVacancies = new ArrayList<>();

// for (String term : searchTerms) {
// List<Vacancy> termVacancies = vacancyRepository
// .findByTitleContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(term,
// term, term);
// matchingVacancies.addAll(termVacancies);
// }

// return matchingVacancies;
// }

// public List<Vacancy> getAllVacancies() {
// return vacancyRepository.findAllByOrderByUpdatedAtDesc();
// }

// public Optional<Vacancy> getVacancyById(UUID id) { // Change Long to UUID
// return vacancyRepository.findById(id);
// }

// public Vacancy createVacancy(Vacancy vacancy) {
// return vacancyRepository.save(vacancy);
// }

// public Vacancy updateVacancy(UUID id, Vacancy vacancy) { // Change Long to
// UUID
// Optional<Vacancy> existingVacancyOptional = vacancyRepository.findById(id);
// if (existingVacancyOptional.isPresent()) {
// vacancy.setId(id);
// return vacancyRepository.save(vacancy);
// } else {
// throw new IllegalArgumentException("Vacancy with ID " + id + " does not
// exist.");
// }
// }

// public void deleteVacancy(UUID id) { // Change Long to UUID
// Optional<Vacancy> existingVacancyOptional = vacancyRepository.findById(id);
// if (existingVacancyOptional.isPresent()) {
// vacancyRepository.deleteById(id);
// } else {
// throw new IllegalArgumentException("Vacancy with ID " + id + " does not
// exist.");
// }
// }
// }

// ! Below code is good but id is still Long
// package com.example.matching.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.example.matching.model.Vacancy;
// import com.example.matching.repository.VacancyRepository;

// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Optional;
// import java.util.Set;

// @Service
// public class VacancyService {
// @Autowired
// private VacancyRepository vacancyRepository;

// // Search in Vacancy Position and Skills
// public List<Vacancy> searchVacancies(List<String> searchTerms) {
// List<Vacancy> matchingVacancies = new ArrayList<>();

// for (String term : searchTerms) {
// List<Vacancy> termVacancies = vacancyRepository
// .findByTitleContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(term,
// term, term);
// matchingVacancies.addAll(termVacancies);
// }

// return matchingVacancies;
// }
// // public List<Vacancy> searchVacancies(String position, String skills) {
// // // Check if both position and skills are provided
// // if (position != null && skills != null) {
// // // return
// //
// vacancyRepository.findByPositionContainingIgnoreCaseAndSkillsContainingIgnoreCase(position,
// // skills);
// // String[] skillArray = skills.split(",");
// // Set<Vacancy> matchingVacanciesPositionAndSkills = new HashSet<>();
// // for (String skill : skillArray) {
// // // List<Vacancy> vacancies =
// // vacancyRepository.findBySkillsContainingIgnoreCase(skill.trim());
// // List<Vacancy> vacancies =
// //
// vacancyRepository.findByPositionContainingIgnoreCaseAndSkillsContainingIgnoreCase(position,
// // skill.trim());
// // matchingVacanciesPositionAndSkills.addAll(vacancies);
// // }
// // return new ArrayList<>(matchingVacanciesPositionAndSkills);
// // }
// // // Check if only position is provided
// // else if (position != null) {
// // return vacancyRepository.findByPositionContainingIgnoreCase(position);
// // }
// // // Check if only skills are provided
// // else if (skills != null) {
// // // Split the input string containing skills into individual skill values
// // String[] skillArray = skills.split(",");
// // Set<Vacancy> matchingVacancies = new HashSet<>();
// // for (String skill : skillArray) {
// // List<Vacancy> vacancies =
// // vacancyRepository.findBySkillsContainingIgnoreCase(skill.trim());
// // matchingVacancies.addAll(vacancies);
// // }
// // return new ArrayList<>(matchingVacancies);
// // }
// // // If neither position nor skills are provided, return all vacancies
// // else {
// // return vacancyRepository.findAll();
// // }
// // }

// // public List<Vacancy> getAllVacancies() {
// // return vacancyRepository.findAll();
// // }
// public List<Vacancy> getAllVacancies() {
// return vacancyRepository.findAllByOrderByUpdatedAtDesc();
// }

// public Optional<Vacancy> getVacancyById(Long id) {
// return vacancyRepository.findById(id);
// }

// public Vacancy createVacancy(Vacancy vacancy) {
// return vacancyRepository.save(vacancy);
// }

// public Vacancy updateVacancy(Long id, Vacancy vacancy) {
// Optional<Vacancy> existingVacancyOptional = vacancyRepository.findById(id);
// if (existingVacancyOptional.isPresent()) {
// vacancy.setId(id);
// return vacancyRepository.save(vacancy);
// } else {
// throw new IllegalArgumentException("Vacancy with ID " + id + " does not
// exist.");
// }
// }

// public void deleteVacancy(Long id) {
// Optional<Vacancy> existingVacancyOptional = vacancyRepository.findById(id);
// if (existingVacancyOptional.isPresent()) {
// vacancyRepository.deleteById(id);
// } else {
// throw new IllegalArgumentException("Vacancy with ID " + id + " does not
// exist.");
// }
// }
// }
