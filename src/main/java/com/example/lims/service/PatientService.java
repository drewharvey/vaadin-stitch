package com.example.lims.service;

import com.example.lims.service.dto.PatientDto;
import java.util.List;
import java.util.Optional;

public interface PatientService {
    List<PatientDto> findAll();
    List<PatientDto> search(String query);
    Optional<PatientDto> findById(Long id);
    PatientDto save(PatientDto dto);
    void deleteById(Long id);
    long count();
}
