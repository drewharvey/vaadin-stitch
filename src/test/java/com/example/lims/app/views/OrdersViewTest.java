package com.example.lims.app.views;

import com.example.lims.app.Application;
import com.example.lims.persistence.entity.OrderStatus;
import com.example.lims.service.LabOrderService;
import com.example.lims.service.LabTestService;
import com.example.lims.service.PatientService;
import com.example.lims.service.dto.LabOrderDto;
import com.example.lims.service.dto.LabTestDto;
import com.example.lims.service.dto.PatientDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
class OrdersViewTest {

    @Autowired
    private LabOrderService orderService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private LabTestService labTestService;

    @Test
    void testCreateOrder() {
        PatientDto patient = patientService.findAll().get(0);
        LabTestDto test = labTestService.findAll().get(0);
        LabOrderDto order = orderService.createOrder(patient.getId(), test.getId());
        assertNotNull(order.getId());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(patient.getId(), order.getPatientId());
        assertEquals(test.getId(), order.getLabTestId());
        orderService.deleteById(order.getId());
    }

    @Test
    void testUpdateOrderStatus() {
        PatientDto patient = patientService.findAll().get(0);
        LabTestDto test = labTestService.findAll().get(0);
        LabOrderDto order = orderService.createOrder(patient.getId(), test.getId());
        LabOrderDto updated = orderService.updateOrder(order.getId(), OrderStatus.IN_PROGRESS, "Processing the sample");
        assertEquals(OrderStatus.IN_PROGRESS, updated.getStatus());
        assertEquals("Processing the sample", updated.getNotes());
        orderService.deleteById(order.getId());
    }

    @Test
    void testUpdateOrderNotes() {
        PatientDto patient = patientService.findAll().get(0);
        LabTestDto test = labTestService.findAll().get(0);
        LabOrderDto order = orderService.createOrder(patient.getId(), test.getId());
        LabOrderDto updated = orderService.updateOrder(order.getId(), OrderStatus.COMPLETED, "Results are normal");
        assertEquals("Results are normal", updated.getNotes());
        assertEquals(OrderStatus.COMPLETED, updated.getStatus());
        orderService.deleteById(order.getId());
    }

    @Test
    void testDeleteOrder() {
        PatientDto patient = patientService.findAll().get(0);
        LabTestDto test = labTestService.findAll().get(0);
        LabOrderDto order = orderService.createOrder(patient.getId(), test.getId());
        Long id = order.getId();
        orderService.deleteById(id);
        assertTrue(orderService.findById(id).isEmpty());
    }

    @Test
    void testFindOrdersByPatient() {
        PatientDto patient = patientService.findAll().get(0);
        LabTestDto test = labTestService.findAll().get(0);
        LabOrderDto order = orderService.createOrder(patient.getId(), test.getId());
        var orders = orderService.findByPatientId(patient.getId());
        assertFalse(orders.isEmpty());
        assertTrue(orders.stream().anyMatch(o -> o.getId().equals(order.getId())));
        orderService.deleteById(order.getId());
    }
}
