package com.example.lims.service;

import com.example.lims.service.dto.LabTestDto;
import java.util.List;
import java.util.Optional;

public interface LabTestService {
    List<LabTestDto> findAll();
    List<LabTestDto> search(String query);
    Optional<LabTestDto> findById(Long id);
    LabTestDto save(LabTestDto dto);
    void deleteById(Long id);
    long count();
}
