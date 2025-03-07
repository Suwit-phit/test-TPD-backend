package com.example.matching.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.matching.model.Vacancy;

import java.util.List;
import java.util.UUID;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, UUID> {
    List<Vacancy> findByPositionTitleContainingIgnoreCaseAndSkillsContainingAllIgnoreCase(String positionTitle, String... skills);
    List<Vacancy> findByPositionTitleContainingIgnoreCaseAndSkillsContainingIgnoreCase(String positionTitle, String skills);
    List<Vacancy> findByPositionTitleContainingIgnoreCase(String positionTitle);
    List<Vacancy> findBySkillsContainingIgnoreCase(String skills);
    List<Vacancy> findByPositionTitleContainingIgnoreCaseOrPositionAppliedContainingIgnoreCaseOrSkillsContainingIgnoreCase(String positionTitle, String positionApplied, String skills);
    List<Vacancy> findAllByOrderByUpdatedAtDesc();
}


//! Below code is good but it doesn't include every that I have set in the "model Vacancy"
// package com.example.matching.repository;

// import java.util.List;
// import java.util.UUID;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import com.example.matching.model.Vacancy;

// @Repository
// public interface VacancyRepository extends JpaRepository<Vacancy, UUID> {  // Change Long to UUID

//     List<Vacancy> findByPositionContainingIgnoreCaseAndSkillsContainingAllIgnoreCase(String position, String... skills);

//     List<Vacancy> findByPositionContainingIgnoreCaseAndSkillsContainingIgnoreCase(String position, String skills);

//     List<Vacancy> findByPositionContainingIgnoreCase(String position);

//     List<Vacancy> findBySkillsContainingIgnoreCase(String skills);

//     List<Vacancy> findByTitleContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(
//             String title, String position, String skills);

//     List<Vacancy> findAllByOrderByUpdatedAtDesc();
// }


//! Below code is good but id is still Long
// package com.example.matching.repository;

// import java.util.List;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.Repository;

// import com.example.matching.model.Candidate;
// import com.example.matching.model.Vacancy;

// @Repository
// public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

//     // Search in Vacancy Postion and Skills
//     // @Query("SELECT v FROM Vacancy v WHERE LOWER(v.position) LIKE
//     // LOWER(CONCAT('%', :position, '%')) AND v.skills IN :skills")
//     // List<Vacancy> findByPositionContainingIgnoreCaseAndSkillsIn(String position,
//     // List<String> skills);

//     List<Vacancy> findByPositionContainingIgnoreCaseAndSkillsContainingAllIgnoreCase(String position, String... skills);

//     List<Vacancy> findByPositionContainingIgnoreCaseAndSkillsContainingIgnoreCase(String position, String skills);

//     List<Vacancy> findByPositionContainingIgnoreCase(String position);

//     List<Vacancy> findBySkillsContainingIgnoreCase(String skills);

//     // List<Vacancy> findByPosition(String position);
//     // List<Vacancy> findBySkillsContaining(String skills);
//     // List<Vacancy> findByPositionAndSkills(String position, String skills);
//     List<Vacancy> findByTitleContainingIgnoreCaseOrPositionContainingIgnoreCaseOrSkillsContainingIgnoreCase(
//             String title, String position, String skills);

//     List<Vacancy> findAllByOrderByUpdatedAtDesc();
// }
