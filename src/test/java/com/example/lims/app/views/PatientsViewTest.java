package com.example.lims.app.views;

import com.example.lims.app.Application;
import com.example.lims.service.PatientService;
import com.example.lims.service.dto.PatientDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
class PatientsViewTest {

    @Autowired
    private PatientService patientService;

    @Test
    void testPatientCountIsAtLeast100() {
        long count = patientService.count();
        assertTrue(count >= 100, "Expected at least 100 patients but got: " + count);
    }

    @Test
    void testSaveNewPatient() {
        PatientDto dto = new PatientDto();
        dto.setFirstName("Test");
        dto.setLastName("Patient");
        dto.setEmail("test@example.com");
        PatientDto saved = patientService.save(dto);
        assertNotNull(saved.getId());
        assertEquals("Test", saved.getFirstName());
        assertEquals("Patient", saved.getLastName());
        patientService.deleteById(saved.getId());
    }

    @Test
    void testDeletePatient() {
        PatientDto dto = new PatientDto();
        dto.setFirstName("ToDelete");
        dto.setLastName("Patient");
        PatientDto saved = patientService.save(dto);
        Long id = saved.getId();
        assertNotNull(id);
        patientService.deleteById(id);
        assertTrue(patientService.findById(id).isEmpty());
    }

    @Test
    void testSearchPatients() {
        var results = patientService.search("Smith");
        assertNotNull(results);
    }
}
