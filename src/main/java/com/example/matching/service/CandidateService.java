package com.example.matching.service;

import com.example.matching.model.Candidate;
import com.example.matching.repository.CandidateRepository;
import com.example.matching.repository.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CommentRepository commentRepository;


    // public List<Candidate> searchCandidates(List<String> searchTerms) {
    // Set<Candidate> matchingCandidates = new HashSet<>();

    // for (String term : searchTerms) {
    // // Search repository for matching candidates, case-insensitive for skills
    // matchingCandidates.addAll(candidateRepository
    // .findByCandidateNameContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(
    // term, term, term));

    // // Additional case-insensitive search for skills using custom query
    // matchingCandidates.addAll(candidateRepository.searchBySkillsIgnoreCase(term));
    // }

    // if (matchingCandidates.isEmpty()) {
    // throw new IllegalArgumentException("No candidates match the search
    // criteria.");
    // }

    // return new ArrayList<>(matchingCandidates); // Convert Set to List to return
    // }
    public List<Candidate> searchCandidates(List<String> searchTerms) {
        Set<Candidate> matchingCandidates = new HashSet<>();

        for (String term : searchTerms) {
            // Search repository for matching candidates, case-insensitive for skills
            matchingCandidates.addAll(candidateRepository
                    .findByCandidateNameContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(
                            term, term, term));

            // Additional case-insensitive search for skills using custom query
            matchingCandidates.addAll(candidateRepository.searchBySkillsIgnoreCase(term));
        }

        if (matchingCandidates.isEmpty()) {
            throw new IllegalArgumentException(
                    "No candidates match the search criteria: " + String.join(", ", searchTerms));
        }

        return new ArrayList<>(matchingCandidates); // Convert Set to List to return
    }

    // Get all candidates
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    // Get candidate by ID (UUID)
    public Optional<Candidate> getCandidateById(UUID id) {
        return candidateRepository.findById(id);
    }

    // Create a new candidate profile
    public Candidate createCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    // Update candidate profile by ID (UUID)
    public Candidate updateCandidate(UUID id, Candidate candidateDetails) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        candidate.setCourtesyTitle(candidateDetails.getCourtesyTitle());
        candidate.setCandidateName(candidateDetails.getCandidateName());
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
        candidate.setImages(candidateDetails.getImages());

        return candidateRepository.save(candidate);
    }

    // Delete candidate profile by ID (UUID)
    // public void deleteCandidate(UUID id) {
    // Candidate candidate = candidateRepository.findById(id)
    // .orElseThrow(() -> new RuntimeException("Candidate not found"));
    // candidateRepository.delete(candidate);
    // }
    @Transactional
    public void deleteCandidate(UUID id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        // Delete all comments associated with the candidate
        commentRepository.deleteAllByCandidateId(id);

        // Then delete the candidate
        candidateRepository.delete(candidate);
    }

}

// ! Below code is the same with upper but with comments
// package com.example.matching.service;

// import com.example.matching.model.Candidate;
// import com.example.matching.repository.CandidateRepository;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Optional;
// import java.util.Set;
// import java.util.UUID;

// @Service
// public class CandidateService {

// @Autowired
// private CandidateRepository candidateRepository;

// // public List<Candidate> searchCandidates(List<String> searchTerms) {
// // Set<Candidate> matchingCandidates = new HashSet<>();

// // for (String term : searchTerms) {
// // // Search repository for matching candidates, case-insensitive for skills
// // matchingCandidates.addAll(candidateRepository
// //
// .findByCandidateNameContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(
// // term, term, term));

// // // Additional case-insensitive search for skills using custom query
// //
// matchingCandidates.addAll(candidateRepository.searchBySkillsIgnoreCase(term));
// // }

// // if (matchingCandidates.isEmpty()) {
// // throw new IllegalArgumentException("No candidates match the search
// // criteria.");
// // }

// // return new ArrayList<>(matchingCandidates); // Convert Set to List to
// return
// // }
// public List<Candidate> searchCandidates(List<String> searchTerms) {
// Set<Candidate> matchingCandidates = new HashSet<>();

// for (String term : searchTerms) {
// // Search repository for matching candidates, case-insensitive for skills
// matchingCandidates.addAll(candidateRepository
// .findByCandidateNameContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(
// term, term, term));

// // Additional case-insensitive search for skills using custom query
// matchingCandidates.addAll(candidateRepository.searchBySkillsIgnoreCase(term));
// }

// if (matchingCandidates.isEmpty()) {
// throw new IllegalArgumentException("No candidates match the search
// criteria.");
// }

// return new ArrayList<>(matchingCandidates); // Convert Set to List to return
// }

// // Method to return all candidates when no search terms are provided
// // public List<Candidate> getAllCandidates() {
// // return candidateRepository.findAllByOrderByUpdatedAtDesc(); // Or use
// another method that suits your ordering
// // // needs
// // }

// // catch (IOException e) {
// // e.printStackTrace();
// // return null;
// // }

// // Get all candidates
// public List<Candidate> getAllCandidates() {
// return candidateRepository.findAll();
// }

// // Get candidate by ID (UUID)
// public Optional<Candidate> getCandidateById(UUID id) {
// return candidateRepository.findById(id);
// }

// // Create a new candidate profile
// public Candidate createCandidate(Candidate candidate) {
// return candidateRepository.save(candidate);
// }

// // Update candidate profile by ID (UUID)
// public Candidate updateCandidate(UUID id, Candidate candidateDetails) {
// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new RuntimeException("Candidate not found"));

// candidate.setCourtesyTitle(candidateDetails.getCourtesyTitle());
// candidate.setCandidateName(candidateDetails.getCandidateName());
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
// candidate.setImages(candidateDetails.getImages());

// return candidateRepository.save(candidate);
// }

// // Delete candidate profile by ID (UUID)
// public void deleteCandidate(UUID id) {
// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new RuntimeException("Candidate not found"));
// candidateRepository.delete(candidate);
// }
// }

// ! Below code is the same with the upper code but without comments
// package com.example.matching.service;

// import com.example.matching.model.Candidate;
// import com.example.matching.repository.CandidateRepository;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Optional;
// import java.util.Set;
// import java.util.UUID;

// @Service
// public class CandidateService {

// @Autowired
// private CandidateRepository candidateRepository;

// // Search by candidate name, position, or skills using multiple search terms
// // public List<Candidate> searchCandidates(List<String> searchTerms) {
// // List<Candidate> matchingCandidates = new ArrayList<>();

// // for (String term : searchTerms) {
// // List<Candidate> termCandidates = candidateRepository
// //
// .findByCandidateNameContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(
// // term, term, term);
// // matchingCandidates.addAll(termCandidates);
// // }

// // if (matchingCandidates.isEmpty()) {
// // throw new IllegalArgumentException("No candidates match the given search
// // criteria.");
// // }
// // return matchingCandidates;
// // }
// // public List<Candidate> searchCandidates(List<String> searchTerms) {
// // Set<Candidate> matchingCandidates = new HashSet<>(); // Use Set to avoid
// // duplicates

// // for (String term : searchTerms) {
// // List<Candidate> termCandidates = candidateRepository
// //
// .findByCandidateNameContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(
// // term, term, term);
// // matchingCandidates.addAll(termCandidates);
// // }

// // if (matchingCandidates.isEmpty()) {
// // throw new IllegalArgumentException("No candidates match the given search
// // criteria.");
// // }
// // return new ArrayList<>(matchingCandidates); // Convert Set back to List
// // }
// // public List<Candidate> searchCandidates(List<String> searchTerms) {
// // Set<Candidate> matchingCandidates = new HashSet<>();
// // for (String term : searchTerms) {
// // matchingCandidates.addAll(candidateRepository
// //
// .findByCandidateNameContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(
// // term, term, term));
// // }
// // if (matchingCandidates.isEmpty()) {
// // throw new IllegalArgumentException("No candidates match the search
// // criteria.");
// // }
// // return new ArrayList<>(matchingCandidates);
// // }
// // public List<Candidate> searchCandidates(List<String> searchTerms) {
// // Set<Candidate> matchingCandidates = new HashSet<>();

// // for (String term : searchTerms) {
// // // Search repository for matching candidates, case-insensitive
// // matchingCandidates.addAll(candidateRepository
// //
// .findByCandidateNameContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(
// // term, term, term));
// // }

// // if (matchingCandidates.isEmpty()) {
// // throw new IllegalArgumentException("No candidates match the search
// // criteria.");
// // }

// // return new ArrayList<>(matchingCandidates); // Convert Set to List to
// return
// // }
// // public List<Candidate> searchCandidates(List<String> searchTerms) {
// // Set<Candidate> matchingCandidates = new HashSet<>();

// // for (String term : searchTerms) {
// // // Search repository for matching candidates, case-insensitive
// // // Ensure all terms are handled in lowercase for consistent matching
// // matchingCandidates.addAll(candidateRepository
// //
// .findByCandidateNameContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(
// // term, term, term));
// // }

// // if (matchingCandidates.isEmpty()) {
// // throw new IllegalArgumentException("No candidates match the search
// // criteria.");
// // }

// // return new ArrayList<>(matchingCandidates); // Convert Set to List to
// return
// // }
// public List<Candidate> searchCandidates(List<String> searchTerms) {
// Set<Candidate> matchingCandidates = new HashSet<>();

// for (String term : searchTerms) {
// // Search repository for matching candidates, case-insensitive for skills
// matchingCandidates.addAll(candidateRepository
// .findByCandidateNameContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(
// term, term, term));

// // Additional case-insensitive search for skills using custom query
// matchingCandidates.addAll(candidateRepository.searchBySkillsIgnoreCase(term));
// }

// if (matchingCandidates.isEmpty()) {
// throw new IllegalArgumentException("No candidates match the search
// criteria.");
// }

// return new ArrayList<>(matchingCandidates); // Convert Set to List to return
// }

// // catch (IOException e) {
// // e.printStackTrace();
// // return null;
// // }

// // Get all candidates
// public List<Candidate> getAllCandidates() {
// return candidateRepository.findAll();
// }

// // Get candidate by ID (UUID)
// public Optional<Candidate> getCandidateById(UUID id) {
// return candidateRepository.findById(id);
// }

// // Create a new candidate profile
// public Candidate createCandidate(Candidate candidate) {
// return candidateRepository.save(candidate);
// }

// // Update candidate profile by ID (UUID)
// public Candidate updateCandidate(UUID id, Candidate candidateDetails) {
// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new RuntimeException("Candidate not found"));

// candidate.setCourtesyTitle(candidateDetails.getCourtesyTitle());
// candidate.setCandidateName(candidateDetails.getCandidateName());
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
// candidate.setImages(candidateDetails.getImages());

// return candidateRepository.save(candidate);
// }

// // Delete candidate profile by ID (UUID)
// public void deleteCandidate(UUID id) {
// Candidate candidate = candidateRepository.findById(id)
// .orElseThrow(() -> new RuntimeException("Candidate not found"));
// candidateRepository.delete(candidate);
// }
// }

// ! below code is good but not include every data
// package com.example.matching.service;

// import com.example.matching.model.Candidate;
// import com.example.matching.repository.CandidateRepository;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;

// @Service
// public class CandidateService {

// @Autowired
// private CandidateRepository candidateRepository;

// // public List<Candidate> searchCandidates(String query) {
// // return
// //
// candidateRepository.findByNameContainingIgnoreCaseOrSkillsContainingIgnoreCase(query,
// // query);
// // }

// // // Search in Candidate Position and Skills
// public List<Candidate> searchCandidates(List<String> searchTerms) {
// List<Candidate> matchingCandidates = new ArrayList<>();

// for (String term : searchTerms) {
// List<Candidate> termCandidates = candidateRepository
// .findByNameContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(term,
// term, term);
// matchingCandidates.addAll(termCandidates);
// }
// if (matchingCandidates.isEmpty()) {
// throw new IllegalArgumentException("No vacancies match the given search
// criteria.");
// }
// return matchingCandidates;
// }

// public List<Candidate> getAllCandidates() {
// return candidateRepository.findAll();
// }

// public Optional<Candidate> getCandidateById(Long id) {
// return candidateRepository.findById(id);
// }

// // public Candidate createCandidate(String name, String position, String
// skills,
// // byte[] resume) {
// // Candidate candidate = new Candidate.builder(name, position, skills,
// resume);
// // return candidateRepository.save(candidate);
// // }
// public Candidate createCandidate(String name, String position, String skills,
// byte[] resume) {
// Candidate candidate = Candidate.builder()
// .name(name)
// .position(position)
// .skills(skills)
// .resume(resume)
// .build();
// return candidateRepository.save(candidate);
// }

// public Candidate updateCandidate(Long id, String name, String position,
// String skills, byte[] resume) {
// Optional<Candidate> existingCandidate = candidateRepository.findById(id);

// if (existingCandidate.isPresent()) {
// Candidate candidate = existingCandidate.get();
// candidate.setName(name);
// candidate.setPosition(position);
// candidate.setSkills(skills);
// if (resume != null) {
// candidate.setResume(resume);
// }
// return candidateRepository.save(candidate);
// } else {
// throw new IllegalArgumentException("Candidate with ID " + id + " does not
// exist.");
// }
// }

// public void deleteCandidate(Long id) {
// Optional<Candidate> candidate = candidateRepository.findById(id);
// candidate.ifPresent(candidateRepository::delete);
// }
// }

// ! Below code is good but not include with upload CvResume
// package com.example.matching.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.example.matching.model.Candidate;
// import com.example.matching.model.Vacancy;
// import com.example.matching.repository.CandidateRepository;

// import java.util.ArrayList;
// import java.util.Date;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Optional;
// import java.util.Set;

// @Service
// public class CandidateService {
// @Autowired
// private CandidateRepository candidateRepository;

// // Search in Candidate Position and Skills
// public List<Candidate> searchCandidates(List<String> searchTerms) {
// List<Candidate> matchingCandidates = new ArrayList<>();

// for (String term : searchTerms) {
// List<Candidate> termCandidates = candidateRepository
// .findByNameContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(term,
// term, term);
// matchingCandidates.addAll(termCandidates);
// }

// return matchingCandidates;
// }

// public List<Candidate> getAllCandidates() {
// return candidateRepository.findAll();
// // return candidateRepository.findAllByOrderByTimestamp();
// }

// public Optional<Candidate> getCandidateById(Long id) {
// return candidateRepository.findById(id);
// }

// public Candidate createCandidate(Candidate candidate) {
// candidate.setTimestamp(new Date());
// return candidateRepository.save(candidate);
// }

// public Candidate updateCandidate(Long id, Candidate candidate) {
// Optional<Candidate> existingCandidateOptional =
// candidateRepository.findById(id);
// if (existingCandidateOptional.isPresent()) {
// candidate.setId(id);
// // Update the timestamp to indicate when the candidate was last updated
// candidate.setTimestamp(new Date());
// return candidateRepository.save(candidate);
// } else {
// throw new IllegalArgumentException("Candidate with ID " + id + " does not
// exist.");
// }
// }

// public void deleteCandidate(Long id) {
// Optional<Candidate> existingCandidateOptional =
// candidateRepository.findById(id);
// if (existingCandidateOptional.isPresent()) {
// candidateRepository.deleteById(id);
// } else {
// throw new IllegalArgumentException("Candidate with ID " + id + " does not
// exist.");
// }
// }
// }

// ! End
// package com.example.matching.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.example.matching.model.Candidate;
// import com.example.matching.model.Vacancy;
// import com.example.matching.repository.CandidateRepository;

// import java.util.ArrayList;
// import java.util.Date;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Optional;
// import java.util.Set;

// @Service
// public class CandidateService {
// @Autowired
// private CandidateRepository candidateRepository;

// // Search in Candidate Position and Skills
// public List<Candidate> searchCandidates(List<String> searchTerms) {
// List<Candidate> matchingCandidates = new ArrayList<>();

// for (String term : searchTerms) {
// List<Candidate> termCandidates = candidateRepository
// .findByNameContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(term,
// term, term);
// matchingCandidates.addAll(termCandidates);
// }

// return matchingCandidates;
// }
// // public List<Candidate> searchCandidates(String name, String skills) {
// // if (name != null && skills != null) {
// // String[] skillArray = skills.split(",");
// // Set<Candidate> matchingCandidatesNameAndSkills = new HashSet<>();
// // for (String skill : skillArray) {
// // List<Candidate> candidates = candidateRepository
// // .findByNameContainingIgnoreCaseAndSkillsContainingIgnoreCase(name,
// // skill.trim());
// // matchingCandidatesNameAndSkills.addAll(candidates);
// // }
// // return new ArrayList<>(matchingCandidatesNameAndSkills);
// // }
// // else if (name != null) {
// // return candidateRepository.findByNameContainingIgnoreCase(name);
// // }
// // else if (skills != null) {
// // String[] skillArray = skills.split(",");
// // Set<Candidate> matchingCandidates = new HashSet<>();
// // for (String skill : skillArray) {
// // List<Candidate> candidates =
// // candidateRepository.findBySkillsContainingIgnoreCase(skill.trim());
// // matchingCandidates.addAll(candidates);
// // }
// // return new ArrayList<>(matchingCandidates);
// // }
// // else {
// // return candidateRepository.findAll();
// // }
// // }

// public List<Candidate> getAllCandidates() {
// return candidateRepository.findAll();
// // return candidateRepository.findAllByOrderByTimestamp();
// }

// public Optional<Candidate> getCandidateById(Long id) {
// return candidateRepository.findById(id);
// }

// public Candidate createCandidate(Candidate candidate) {
// candidate.setTimestamp(new Date());
// return candidateRepository.save(candidate);
// }

// // public Candidate updateCandidate(Long id, Candidate candidate) {
// // candidate.setId(id);
// // return candidateRepository.save(candidate);
// // }
// public Candidate updateCandidate(Long id, Candidate candidate) {
// Optional<Candidate> existingCandidateOptional =
// candidateRepository.findById(id);
// if (existingCandidateOptional.isPresent()) {
// candidate.setId(id);
// // Update the timestamp to indicate when the candidate was last updated
// candidate.setTimestamp(new Date());
// return candidateRepository.save(candidate);
// } else {
// throw new IllegalArgumentException("Candidate with ID " + id + " does not
// exist.");
// }
// }

// // public void deleteCandidate(Long id) {
// // candidateRepository.deleteById(id);
// // }
// public void deleteCandidate(Long id) {
// Optional<Candidate> existingCandidateOptional =
// candidateRepository.findById(id);
// if (existingCandidateOptional.isPresent()) {
// candidateRepository.deleteById(id);
// } else {
// throw new IllegalArgumentException("Candidate with ID " + id + " does not
// exist.");
// }
// }
// }
