package com.FKFabian.medicalclinicproxy123.remote;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class PatientDTO {
    private final String email;
    private final String idCardNo;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final LocalDate birthday;
    private final List<VisitDto> visits;
}
