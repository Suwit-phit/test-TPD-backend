package com.example.matching.repository;

import com.example.matching.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, UUID> {

    @Query("SELECT c FROM Candidate c JOIN c.skills s WHERE LOWER(s) LIKE LOWER(CONCAT('%', :skill, '%'))")
    List<Candidate> searchBySkillsIgnoreCase(@Param("skill") String skill);

    // Query methods for candidate searching
    List<Candidate> findByCandidateNameContainingIgnoreCaseAndSkillsContainingAllIgnoreCase(String candidateName,
            String... skills);

    List<Candidate> findByCandidateNameContainingIgnoreCaseAndSkillsContainingIgnoreCase(String candidateName,
            String skills);

    List<Candidate> findByCandidateNameContainingIgnoreCase(String candidateName);

    List<Candidate> findBySkillsContainingIgnoreCase(String skills);

    List<Candidate> findByCandidateNameContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(
            String candidateName, String position, String skills);

    // Additional query methods similar to UserProfileRepository
    List<Candidate> findAllByOrderByUpdatedAtDesc();

    List<Candidate> findAllByCandidateName(String candidateName);

    Optional<Candidate> findByIdAndCandidateName(UUID id, String candidateName);
}

// ! below code is good but not include every data
// package com.example.matching.repository;

// import java.util.List;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import com.example.matching.model.Candidate;
// import com.example.matching.model.Vacancy;

// @Repository
// public interface CandidateRepository extends JpaRepository<Candidate, Long> {

// List<Candidate>
// findByNameContainingIgnoreCaseAndSkillsContainingAllIgnoreCase(String name,
// String... skills);

// List<Candidate>
// findByNameContainingIgnoreCaseAndSkillsContainingIgnoreCase(String name,
// String skills);

// List<Candidate> findByNameContainingIgnoreCase(String name);

// List<Candidate> findBySkillsContainingIgnoreCase(String skills);

// List<Candidate>
// findByNameContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(
// String name, String position, String skills);
// }

// ! End
// package com.example.matching.repository;

// import java.util.List;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import com.example.matching.model.Candidate;
// import com.example.matching.model.Vacancy;

// @Repository
// public interface CandidateRepository extends JpaRepository<Candidate, Long> {
// // List<Candidate> findAllByOrderByTimestamp();
// // List<Candidate>
// findByPositionContainingIgnoreCaseAndSkillsContainingAllIgnoreCase(String
// position,
// // String... skills);

// List<Candidate>
// findByNameContainingIgnoreCaseAndSkillsContainingAllIgnoreCase(String name,
// String... skills);

// // List<Candidate>
// findByPositionContainingIgnoreCaseAndSkillsContainingIgnoreCase(String
// position, String skills);

// List<Candidate>
// findByNameContainingIgnoreCaseAndSkillsContainingIgnoreCase(String name,
// String skills);

// // List<Candidate> findByPositionContainingIgnoreCase(String position);

// List<Candidate> findByNameContainingIgnoreCase(String name);

// List<Candidate> findBySkillsContainingIgnoreCase(String skills);

// List<Candidate>
// findByNameContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(
// String name, String position, String skills);
// }
