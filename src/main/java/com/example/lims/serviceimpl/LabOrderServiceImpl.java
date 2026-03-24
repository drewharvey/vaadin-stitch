package com.example.lims.serviceimpl;

import com.example.lims.persistence.LabOrder;
import com.example.lims.persistence.LabOrderRepository;
import com.example.lims.persistence.OrderStatus;
import com.example.lims.persistence.PatientRepository;
import com.example.lims.persistence.LabTestRepository;
import com.example.lims.service.LabOrderService;
import com.example.lims.service.dto.LabOrderDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LabOrderServiceImpl implements LabOrderService {

    private final LabOrderRepository orderRepository;
    private final PatientRepository patientRepository;
    private final LabTestRepository labTestRepository;

    public LabOrderServiceImpl(LabOrderRepository orderRepository,
                               PatientRepository patientRepository,
                               LabTestRepository labTestRepository) {
        this.orderRepository = orderRepository;
        this.patientRepository = patientRepository;
        this.labTestRepository = labTestRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabOrderDto> findAll() {
        return orderRepository.findAllWithDetails().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabOrderDto> findByPatientId(Long patientId) {
        return orderRepository.findByPatientId(patientId).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LabOrderDto> findById(Long id) {
        return orderRepository.findById(id).map(this::toDto);
    }

    @Override
    public LabOrderDto createOrder(Long patientId, Long labTestId) {
        LabOrder order = new LabOrder();
        order.setPatient(patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + patientId)));
        order.setLabTest(labTestRepository.findById(labTestId)
                .orElseThrow(() -> new IllegalArgumentException("Lab test not found: " + labTestId)));
        order.setStatus(OrderStatus.PENDING);
        order.setOrderedAt(LocalDateTime.now());
        return toDto(orderRepository.save(order));
    }

    @Override
    public LabOrderDto updateOrder(Long id, OrderStatus status, String notes) {
        LabOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        if (status != null) order.setStatus(status);
        order.setNotes(notes);
        order.setUpdatedAt(LocalDateTime.now());
        return toDto(orderRepository.save(order));
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return orderRepository.count();
    }

    private LabOrderDto toDto(LabOrder o) {
        LabOrderDto dto = new LabOrderDto();
        dto.setId(o.getId());
        dto.setPatientId(o.getPatient().getId());
        dto.setPatientFullName(o.getPatient().getFirstName() + " " + o.getPatient().getLastName());
        dto.setLabTestId(o.getLabTest().getId());
        dto.setLabTestName(o.getLabTest().getName());
        dto.setLabTestCode(o.getLabTest().getCode());
        dto.setOrderedAt(o.getOrderedAt());
        dto.setStatus(o.getStatus());
        dto.setNotes(o.getNotes());
        dto.setUpdatedAt(o.getUpdatedAt());
        return dto;
    }
}
