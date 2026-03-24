package com.example.lims.service;

import com.example.lims.persistence.OrderStatus;
import com.example.lims.service.dto.LabOrderDto;
import java.util.List;
import java.util.Optional;

public interface LabOrderService {
    List<LabOrderDto> findAll();
    List<LabOrderDto> findByPatientId(Long patientId);
    Optional<LabOrderDto> findById(Long id);
    LabOrderDto createOrder(Long patientId, Long labTestId);
    LabOrderDto updateOrder(Long id, OrderStatus status, String notes);
    void deleteById(Long id);
    long count();
}
