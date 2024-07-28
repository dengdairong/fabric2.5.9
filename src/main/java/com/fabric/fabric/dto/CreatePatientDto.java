package com.fabric.fabric.dto;

import lombok.Data;

@Data
public class CreatePatientDto {
    private String name;
    private String birthDate;
    private String height;
    private String weight;
    private String gender;
    private String contact;
}
