package com.example.lims.serviceimpl;

import com.example.lims.entity.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LabTestRepository extends JpaRepository<LabTest, Long> {
    @Query("SELECT t FROM LabTest t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(t.code) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<LabTest> search(@Param("q") String query);
}
