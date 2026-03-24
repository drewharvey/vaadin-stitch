package com.example.lims.service.dto;

import com.example.lims.entity.OrderStatus;
import java.time.LocalDateTime;

public class LabOrderDto {
    private Long id;
    private Long patientId;
    private String patientFullName;
    private Long labTestId;
    private String labTestName;
    private String labTestCode;
    private LocalDateTime orderedAt;
    private OrderStatus status;
    private String notes;
    private LocalDateTime updatedAt;

    public LabOrderDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public String getPatientFullName() { return patientFullName; }
    public void setPatientFullName(String patientFullName) { this.patientFullName = patientFullName; }
    public Long getLabTestId() { return labTestId; }
    public void setLabTestId(Long labTestId) { this.labTestId = labTestId; }
    public String getLabTestName() { return labTestName; }
    public void setLabTestName(String labTestName) { this.labTestName = labTestName; }
    public String getLabTestCode() { return labTestCode; }
    public void setLabTestCode(String labTestCode) { this.labTestCode = labTestCode; }
    public LocalDateTime getOrderedAt() { return orderedAt; }
    public void setOrderedAt(LocalDateTime orderedAt) { this.orderedAt = orderedAt; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
