package com.example.lims.serviceimpl;

import com.example.lims.entity.LabTest;
import com.example.lims.service.LabTestService;
import com.example.lims.service.dto.LabTestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LabTestServiceImpl implements LabTestService {

    private final LabTestRepository repository;

    public LabTestServiceImpl(LabTestRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTestDto> findAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabTestDto> search(String query) {
        if (query == null || query.isBlank()) return findAll();
        return repository.search(query).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LabTestDto> findById(Long id) {
        return repository.findById(id).map(this::toDto);
    }

    @Override
    public LabTestDto save(LabTestDto dto) {
        LabTest entity = dto.getId() != null
                ? repository.findById(dto.getId()).orElseGet(LabTest::new)
                : new LabTest();
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
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

    private LabTestDto toDto(LabTest t) {
        return new LabTestDto(t.getId(), t.getCode(), t.getName(), t.getDescription(), t.getPrice());
    }
}
