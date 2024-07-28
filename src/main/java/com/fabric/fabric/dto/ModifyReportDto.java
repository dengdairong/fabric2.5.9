package com.fabric.fabric.dto;

import lombok.Data;

import java.util.List;

@Data
public class ModifyReportDto {
    private String hospitalName;
    private String patientName;
    private String symptoms;
    private List<String> neededDrugs;
}
