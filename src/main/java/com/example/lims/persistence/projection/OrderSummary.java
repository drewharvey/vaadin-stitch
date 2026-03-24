package com.example.lims.persistence.projection;

import com.example.lims.persistence.entity.OrderStatus;
import java.time.LocalDateTime;

public interface OrderSummary {
    Long getId();
    String getPatientFirstName();
    String getPatientLastName();
    String getLabTestName();
    String getLabTestCode();
    LocalDateTime getOrderedAt();
    OrderStatus getStatus();
    String getNotes();
}
