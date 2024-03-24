package com.FKFabian.medicalclinicproxy123.remote;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientCreateDto {
    private final String email;
    private final String password;
    private final String idCardNo;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final LocalDate birthday;
}

