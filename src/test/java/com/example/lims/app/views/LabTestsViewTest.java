package com.example.lims.app.views;

import com.example.lims.app.Application;
import com.example.lims.service.LabTestService;
import com.example.lims.service.dto.LabTestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
class LabTestsViewTest {

    @Autowired
    private LabTestService labTestService;

    @Test
    void testLabTestCountIsAtLeast50() {
        long count = labTestService.count();
        assertTrue(count >= 50, "Expected at least 50 lab tests but got: " + count);
    }

    @Test
    void testSaveNewLabTest() {
        LabTestDto dto = new LabTestDto();
        dto.setCode("TEST001");
        dto.setName("Test Lab Test");
        dto.setDescription("A test description");
        dto.setPrice(new BigDecimal("99.99"));
        LabTestDto saved = labTestService.save(dto);
        assertNotNull(saved.getId());
        assertEquals("TEST001", saved.getCode());
        labTestService.deleteById(saved.getId());
    }

    @Test
    void testSearchLabTests() {
        var results = labTestService.search("Blood");
        assertNotNull(results);
    }

    @Test
    void testDeleteLabTest() {
        LabTestDto dto = new LabTestDto();
        dto.setCode("DEL001");
        dto.setName("To Delete");
        LabTestDto saved = labTestService.save(dto);
        Long id = saved.getId();
        labTestService.deleteById(id);
        assertTrue(labTestService.findById(id).isEmpty());
    }
}
