package com.example.lims.persistence.repository;

import com.example.lims.persistence.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Query("SELECT p FROM Patient p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(p.email) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Patient> search(@Param("q") String query);
}
