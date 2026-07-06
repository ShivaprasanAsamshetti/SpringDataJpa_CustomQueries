package com.example.practicecrud.SBpractice.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class DoctorDto {
    private Integer docterId;
    private  String docterName;
    private String doctorType;
}
