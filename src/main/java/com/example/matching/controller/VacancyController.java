package com.example.matching.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.matching.model.Vacancy;
import com.example.matching.service.VacancyService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.UUID;

@RestController
@RequestMapping("/vacancies")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class VacancyController {

    @Autowired
    private VacancyService vacancyService;

    @GetMapping("/search")
    public ResponseEntity<Object> searchVacancies(@RequestParam String query) {
        try {
            // Split terms by spaces or commas, trimming each term
            List<String> searchTerms = Arrays.stream(query.split("\\s+|,"))
                    .map(String::trim)
                    .collect(Collectors.toList());

            // Search vacancies using the search terms
            List<Vacancy> matchingVacancies = vacancyService.searchVacancies(searchTerms);
            return new ResponseEntity<>(matchingVacancies, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Handle no vacancies found
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            // Handle other errors
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to search vacancies: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllVacancies() {
        try {
            List<Vacancy> vacancies = vacancyService.getAllVacancies();
            return new ResponseEntity<>(vacancies, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch vacancies: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getVacancyById(@PathVariable UUID id) {
        Optional<Vacancy> vacancy = vacancyService.getVacancyById(id);
        if (vacancy.isPresent()) {
            return new ResponseEntity<>(vacancy.get(), HttpStatus.OK);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Vacancy not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Object> createVacancy(@RequestBody Vacancy vacancy) {
        try {
            Vacancy createdVacancy = vacancyService.createVacancy(vacancy);
            return new ResponseEntity<>(createdVacancy, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Failed to create vacancy: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateVacancy(@PathVariable UUID id, @RequestBody Vacancy vacancy) {
        try {
            Vacancy updatedVacancy = vacancyService.updateVacancy(id, vacancy);
            return new ResponseEntity<>(updatedVacancy, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update vacancy: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteVacancy(@PathVariable UUID id) {
        try {
            vacancyService.deleteVacancy(id);
            return ResponseEntity.ok("Successfully Deleted");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete vacancy: " + e.getMessage()));
        }
    }
}

//! Below code is the same with the upper one but without comments
// package com.example.matching.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.example.matching.model.Vacancy;
// import com.example.matching.service.VacancyService;

// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;
// import java.util.stream.Collectors;
// import java.util.UUID;

// @RestController
// @RequestMapping("/vacancies")
// public class VacancyController {

//     @Autowired
//     private VacancyService vacancyService;

//     // @GetMapping("/search")
//     // public ResponseEntity<Object> searchVacancies(@RequestParam String query) {
//     // try {
//     // List<String> searchTerms =
//     // Arrays.stream(query.split("\\s*,\\s*|\\s+and\\s+"))
//     // .map(String::trim)
//     // .collect(Collectors.toList());
//     // List<Vacancy> matchingVacancies =
//     // vacancyService.searchVacancies(searchTerms);
//     // return new ResponseEntity<>(matchingVacancies, HttpStatus.OK);
//     // } catch (Exception e) {
//     // Map<String, Object> errorResponse = new HashMap<>();
//     // errorResponse.put("error", "Failed to search vacancies: " + e.getMessage());
//     // return
//     // ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//     // }
//     // }
//     @GetMapping("/search")
//     public ResponseEntity<Object> searchVacancies(@RequestParam String query) {
//         try {
//             // Split terms by spaces or commas, trimming each term
//             List<String> searchTerms = Arrays.stream(query.split("\\s+|,"))
//                     .map(String::trim)
//                     .collect(Collectors.toList());

//             // Search vacancies using the search terms
//             List<Vacancy> matchingVacancies = vacancyService.searchVacancies(searchTerms);
//             return new ResponseEntity<>(matchingVacancies, HttpStatus.OK);
//         } catch (IllegalArgumentException e) {
//             // Handle no vacancies found
//             Map<String, Object> errorResponse = new HashMap<>();
//             errorResponse.put("error", e.getMessage());
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
//         } catch (Exception e) {
//             // Handle other errors
//             Map<String, Object> errorResponse = new HashMap<>();
//             errorResponse.put("error", "Failed to search vacancies: " + e.getMessage());
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//         }
//     }

//     @GetMapping
//     public ResponseEntity<Object> getAllVacancies() {
//         try {
//             List<Vacancy> vacancies = vacancyService.getAllVacancies();
//             return new ResponseEntity<>(vacancies, HttpStatus.OK);
//         } catch (Exception e) {
//             Map<String, Object> errorResponse = new HashMap<>();
//             errorResponse.put("error", "Failed to fetch vacancies: " + e.getMessage());
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//         }
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<Object> getVacancyById(@PathVariable UUID id) {
//         Optional<Vacancy> vacancy = vacancyService.getVacancyById(id);
//         if (vacancy.isPresent()) {
//             return new ResponseEntity<>(vacancy.get(), HttpStatus.OK);
//         } else {
//             Map<String, Object> errorResponse = new HashMap<>();
//             errorResponse.put("error", "Vacancy not found");
//             return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
//         }
//     }

//     @PostMapping
//     public ResponseEntity<Object> createVacancy(@RequestBody Vacancy vacancy) {
//         try {
//             Vacancy createdVacancy = vacancyService.createVacancy(vacancy);
//             return new ResponseEntity<>(createdVacancy, HttpStatus.CREATED);
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                     .body(Map.of("error", "Failed to create vacancy: " + e.getMessage()));
//         }
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<Object> updateVacancy(@PathVariable UUID id, @RequestBody Vacancy vacancy) {
//         try {
//             Vacancy updatedVacancy = vacancyService.updateVacancy(id, vacancy);
//             return new ResponseEntity<>(updatedVacancy, HttpStatus.OK);
//         } catch (IllegalArgumentException e) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                     .body(Map.of("error", e.getMessage()));
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                     .body(Map.of("error", "Failed to update vacancy: " + e.getMessage()));
//         }
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Object> deleteVacancy(@PathVariable UUID id) {
//         try {
//             vacancyService.deleteVacancy(id);
//             return ResponseEntity.ok("Successfully Deleted");
//         } catch (IllegalArgumentException e) {
//             return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                     .body(Map.of("error", e.getMessage()));
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                     .body(Map.of("error", "Failed to delete vacancy: " + e.getMessage()));
//         }
//     }
// }

// ! Below code is the same with upper code without comments
// package com.example.matching.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.example.matching.model.Vacancy;
// import com.example.matching.service.VacancyService;

// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;
// import java.util.stream.Collectors;
// import java.util.UUID;

// @RestController
// @RequestMapping("/vacancies")
// public class VacancyController {

// @Autowired
// private VacancyService vacancyService;

// // @GetMapping("/search")
// // public ResponseEntity<List<Vacancy>> searchVacancies(@RequestParam String
// // query) {
// // try {
// // List<String> searchTerms =
// // Arrays.stream(query.split("\\s*,\\s*|\\s+and\\s+"))
// // .map(String::trim)
// // .collect(Collectors.toList());
// // List<Vacancy> matchingVacancies =
// // vacancyService.searchVacancies(searchTerms);
// // return new ResponseEntity<>(matchingVacancies, HttpStatus.OK);
// // } catch (Exception e) {
// // Map<String, Object> errorResponse = new HashMap<>();
// // errorResponse.put("error", "Failed to search vacancies: " +
// e.getMessage());
// // return
// //
// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
// // }
// // }
// @GetMapping("/search")
// public ResponseEntity<Object> searchVacancies(@RequestParam String query) {
// try {
// List<String> searchTerms =
// Arrays.stream(query.split("\\s*,\\s*|\\s+and\\s+"))
// .map(String::trim)
// .collect(Collectors.toList());
// List<Vacancy> matchingVacancies =
// vacancyService.searchVacancies(searchTerms);
// return new ResponseEntity<>(matchingVacancies, HttpStatus.OK);
// } catch (Exception e) {
// Map<String, Object> errorResponse = new HashMap<>();
// errorResponse.put("error", "Failed to search vacancies: " + e.getMessage());
// return
// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
// }
// }

// // @GetMapping
// // public ResponseEntity<List<Vacancy>> getAllVacancies() {
// // try {
// // List<Vacancy> vacancies = vacancyService.getAllVacancies();
// // return new ResponseEntity<>(vacancies, HttpStatus.OK);
// // } catch (Exception e) {
// // Map<String, Object> errorResponse = new HashMap<>();
// // errorResponse.put("error", "Failed to fetch vacancies: " +
// e.getMessage());
// // return
// //
// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
// // }
// // }
// @GetMapping
// public ResponseEntity<Object> getAllVacancies() {
// try {
// List<Vacancy> vacancies = vacancyService.getAllVacancies();
// return new ResponseEntity<>(vacancies, HttpStatus.OK);
// } catch (Exception e) {
// Map<String, Object> errorResponse = new HashMap<>();
// errorResponse.put("error", "Failed to fetch vacancies: " + e.getMessage());
// return
// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
// }
// }

// // @GetMapping("/{id}")
// // public ResponseEntity<Object> getVacancyById(@PathVariable UUID id) {
// // try {
// // Optional<Vacancy> vacancy = vacancyService.getVacancyById(id);
// // return vacancy.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
// // .orElseGet(() -> new ResponseEntity<>(Map.of("error", "Vacancy not
// found"),
// // HttpStatus.NOT_FOUND));
// // } catch (Exception e) {
// // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
// // .body(Map.of("error", "Failed to retrieve vacancy: " + e.getMessage()));
// // }
// // }
// @GetMapping("/{id}")
// public ResponseEntity<Object> getVacancyById(@PathVariable UUID id) {
// Optional<Vacancy> vacancy = vacancyService.getVacancyById(id);
// if (vacancy.isPresent()) {
// return new ResponseEntity<>(vacancy.get(), HttpStatus.OK);
// } else {
// Map<String, Object> errorResponse = new HashMap<>();
// errorResponse.put("error", "Vacancy not found");
// return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
// }
// }

// @PostMapping
// public ResponseEntity<Object> createVacancy(@RequestBody Vacancy vacancy) {
// try {
// Vacancy createdVacancy = vacancyService.createVacancy(vacancy);
// return new ResponseEntity<>(createdVacancy, HttpStatus.CREATED);
// } catch (Exception e) {
// return ResponseEntity.status(HttpStatus.BAD_REQUEST)
// .body(Map.of("error", "Failed to create vacancy: " + e.getMessage()));
// }
// }

// @PutMapping("/{id}")
// public ResponseEntity<Object> updateVacancy(@PathVariable UUID id,
// @RequestBody Vacancy vacancy) {
// try {
// Vacancy updatedVacancy = vacancyService.updateVacancy(id, vacancy);
// return new ResponseEntity<>(updatedVacancy, HttpStatus.OK);
// } catch (IllegalArgumentException e) {
// return ResponseEntity.status(HttpStatus.NOT_FOUND)
// .body(Map.of("error", e.getMessage()));
// } catch (Exception e) {
// return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
// .body(Map.of("error", "Failed to update vacancy: " + e.getMessage()));
// }
// }

// @DeleteMapping("/{id}")
// public ResponseEntity<Object> deleteVacancy(@PathVariable UUID id) {
// try {
// vacancyService.deleteVacancy(id);
// return ResponseEntity.ok("Successfully Deleted");
// } catch (IllegalArgumentException e) {
// return ResponseEntity.status(HttpStatus.NOT_FOUND)
// .body(Map.of("error", e.getMessage()));
// } catch (Exception e) {
// return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
// .body(Map.of("error", "Failed to delete vacancy: " + e.getMessage()));
// }
// }
// }

// ! Below is good but it doesn't try catch error
// package com.example.matching.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.example.matching.model.Candidate;
// import com.example.matching.model.Vacancy;
// import com.example.matching.service.VacancyService;

// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;
// import java.util.stream.Collectors;
// import java.util.UUID;

// @RestController
// @RequestMapping("/vacancies")
// public class VacancyController {
// @Autowired
// private VacancyService vacancyService;

// // @GetMapping("/search")
// // public ResponseEntity<List<Vacancy>> searchVacancies(
// // @RequestParam(required = false) String position,
// // @RequestParam(required = false) String skills) {
// // List<Vacancy> matchingVacancies = vacancyService.searchVacancies(position,
// // skills);
// // return new ResponseEntity<>(matchingVacancies, HttpStatus.OK);
// // }
// @GetMapping("/search")
// public ResponseEntity<List<Vacancy>> searchVacancies(@RequestParam String
// query) {
// // Split the input query string into individual search terms based on "," or
// // "and"
// List<String> searchTerms =
// Arrays.stream(query.split("\\s*,\\s*|\\s+and\\s+"))
// .map(String::trim)
// .collect(Collectors.toList());

// // Search for vacancies based on the search terms
// List<Vacancy> matchingVacancies =
// vacancyService.searchVacancies(searchTerms);

// return new ResponseEntity<>(matchingVacancies, HttpStatus.OK);
// }

// @GetMapping
// public ResponseEntity<List<Vacancy>> getAllVacancies() {
// List<Vacancy> vacancies = vacancyService.getAllVacancies();
// return new ResponseEntity<>(vacancies, HttpStatus.OK);
// }

// @GetMapping("/{id}")
// public ResponseEntity<Vacancy> getVacancyById(@PathVariable UUID id) {
// Optional<Vacancy> vacancy = vacancyService.getVacancyById(id);
// return vacancy.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
// .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
// }

// @PostMapping
// public ResponseEntity<Vacancy> createVacancy(@RequestBody Vacancy vacancy) {
// Vacancy createdVacancy = vacancyService.createVacancy(vacancy);
// return new ResponseEntity<>(createdVacancy, HttpStatus.CREATED);
// }

// @PutMapping("/{id}")
// public ResponseEntity<Object> updateVacancy(@PathVariable UUID id,
// @RequestBody Vacancy vacancy) {
// try {
// Vacancy updatedVacancy = vacancyService.updateVacancy(id, vacancy);
// return new ResponseEntity<>(updatedVacancy, HttpStatus.OK);
// } catch (IllegalArgumentException e) {
// String errorMessage = e.getMessage();
// Map<String, Object> errorResponse = new HashMap<>();
// errorResponse.put("error", errorMessage);
// return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
// }
// }
// // @PutMapping("/{id}")
// // public ResponseEntity<Vacancy> updateVacancy(@PathVariable UUID id,
// // @RequestBody Vacancy vacancy) {
// // Vacancy updatedVacancy = vacancyService.updateVacancy(id, vacancy);
// // return new ResponseEntity<>(updatedVacancy, HttpStatus.OK);
// // }

// @DeleteMapping("/{id}")
// public ResponseEntity<Object> deleteVacancy(@PathVariable UUID id) {
// try {
// vacancyService.deleteVacancy(id);
// // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
// return ResponseEntity.ok("Successfully Deleted");
// } catch (IllegalArgumentException e) {
// // TODO Auto-generated catch block
// String errorMessage = e.getMessage();
// Map<String, String> errorResponse = new HashMap<>();
// errorResponse.put("error", errorMessage);
// return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
// }
// }
// // @DeleteMapping("/{id}")
// // public ResponseEntity<Void> deleteVacancy(@PathVariable UUID id) {
// // vacancyService.deleteVacancy(id);
// // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
// // }
// }

// ? Below code is good but it still UUID
// package com.example.matching.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.example.matching.model.Candidate;
// import com.example.matching.model.Vacancy;
// import com.example.matching.service.VacancyService;

// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;
// import java.util.stream.Collectors;

// @RestController
// @RequestMapping("/vacancies")
// public class VacancyController {
// @Autowired
// private VacancyService vacancyService;

// // @GetMapping("/search")
// // public ResponseEntity<List<Vacancy>> searchVacancies(
// // @RequestParam(required = false) String position,
// // @RequestParam(required = false) String skills) {
// // List<Vacancy> matchingVacancies = vacancyService.searchVacancies(position,
// // skills);
// // return new ResponseEntity<>(matchingVacancies, HttpStatus.OK);
// // }
// @GetMapping("/search")
// public ResponseEntity<List<Vacancy>> searchVacancies(@RequestParam String
// query) {
// // Split the input query string into individual search terms based on "," or
// // "and"
// List<String> searchTerms =
// Arrays.stream(query.split("\\s*,\\s*|\\s+and\\s+"))
// .map(String::trim)
// .collect(Collectors.toList());

// // Search for vacancies based on the search terms
// List<Vacancy> matchingVacancies =
// vacancyService.searchVacancies(searchTerms);

// return new ResponseEntity<>(matchingVacancies, HttpStatus.OK);
// }

// @GetMapping
// public ResponseEntity<List<Vacancy>> getAllVacancies() {
// List<Vacancy> vacancies = vacancyService.getAllVacancies();
// return new ResponseEntity<>(vacancies, HttpStatus.OK);
// }

// @GetMapping("/{id}")
// public ResponseEntity<Vacancy> getVacancyById(@PathVariable Long id) {
// Optional<Vacancy> vacancy = vacancyService.getVacancyById(id);
// return vacancy.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
// .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
// }

// @PostMapping
// public ResponseEntity<Vacancy> createVacancy(@RequestBody Vacancy vacancy) {
// Vacancy createdVacancy = vacancyService.createVacancy(vacancy);
// return new ResponseEntity<>(createdVacancy, HttpStatus.CREATED);
// }

// @PutMapping("/{id}")
// public ResponseEntity<Object> updateVacancy(@PathVariable Long id,
// @RequestBody Vacancy vacancy) {
// try {
// Vacancy updatedVacancy = vacancyService.updateVacancy(id, vacancy);
// return new ResponseEntity<>(updatedVacancy, HttpStatus.OK);
// } catch (IllegalArgumentException e) {
// String errorMessage = e.getMessage();
// Map<String, Object> errorResponse = new HashMap<>();
// errorResponse.put("error", errorMessage);
// return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
// }
// }
// // @PutMapping("/{id}")
// // public ResponseEntity<Vacancy> updateVacancy(@PathVariable Long id,
// // @RequestBody Vacancy vacancy) {
// // Vacancy updatedVacancy = vacancyService.updateVacancy(id, vacancy);
// // return new ResponseEntity<>(updatedVacancy, HttpStatus.OK);
// // }

// @DeleteMapping("/{id}")
// public ResponseEntity<Object> deleteVacancy(@PathVariable Long id) {
// try {
// vacancyService.deleteVacancy(id);
// // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
// return ResponseEntity.ok("Successfully Deleted");
// } catch (IllegalArgumentException e) {
// // TODO Auto-generated catch block
// String errorMessage = e.getMessage();
// Map<String, String> errorResponse = new HashMap<>();
// errorResponse.put("error", errorMessage);
// return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
// }
// }
// // @DeleteMapping("/{id}")
// // public ResponseEntity<Void> deleteVacancy(@PathVariable Long id) {
// // vacancyService.deleteVacancy(id);
// // return new ResponseEntity<>(HttpStatus.NO_CONTENT);
// // }
// }
