package com.example.lims.serviceimpl;

import com.example.lims.persistence.entity.Patient;
import com.example.lims.persistence.repository.PatientRepository;
import com.example.lims.service.PatientService;
import com.example.lims.service.dto.PatientDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;

    public PatientServiceImpl(PatientRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> findAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> search(String query) {
        if (query == null || query.isBlank()) return findAll();
        return repository.search(query).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PatientDto> findById(Long id) {
        return repository.findById(id).map(this::toDto);
    }

    @Override
    public PatientDto save(PatientDto dto) {
        Patient entity = dto.getId() != null
                ? repository.findById(dto.getId()).orElseGet(Patient::new)
                : new Patient();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        return toDto(repository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    private PatientDto toDto(Patient p) {
        return new PatientDto(p.getId(), p.getFirstName(), p.getLastName(),
                p.getDateOfBirth(), p.getEmail(), p.getPhone(), p.getCreatedAt());
    }
}
