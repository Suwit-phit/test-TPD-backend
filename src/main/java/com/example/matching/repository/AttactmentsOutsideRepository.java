package com.example.matching.repository;

// import com.example.demo.model.AttactmentsOutside;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.matching.model.AttactmentsOutside;

import java.util.UUID;

@Repository
public interface AttactmentsOutsideRepository extends JpaRepository<AttactmentsOutside, UUID> {
}

