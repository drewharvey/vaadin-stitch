package com.example.lims.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lab_orders")
public class LabOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lab_test_id", nullable = false)
    private LabTest labTest;

    @Column(nullable = false, updatable = false)
    private LocalDateTime orderedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(length = 2000)
    private String notes;

    @Column
    private LocalDateTime updatedAt;

    public LabOrder() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public LabTest getLabTest() { return labTest; }
    public void setLabTest(LabTest labTest) { this.labTest = labTest; }
    public LocalDateTime getOrderedAt() { return orderedAt; }
    public void setOrderedAt(LocalDateTime orderedAt) { this.orderedAt = orderedAt; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
