package com.example.lims.service.dto;

import java.math.BigDecimal;

public class LabTestDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private BigDecimal price;

    public LabTestDto() {}

    public LabTestDto(Long id, String code, String name, String description, BigDecimal price) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
