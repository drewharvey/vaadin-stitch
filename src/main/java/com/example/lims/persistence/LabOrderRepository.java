package com.example.lims.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface LabOrderRepository extends JpaRepository<LabOrder, Long> {
    @Query("SELECT o FROM LabOrder o JOIN FETCH o.patient JOIN FETCH o.labTest WHERE o.patient.id = :patientId ORDER BY o.orderedAt DESC")
    List<LabOrder> findByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT o FROM LabOrder o JOIN FETCH o.patient JOIN FETCH o.labTest ORDER BY o.orderedAt DESC")
    List<LabOrder> findAllWithDetails();
}
