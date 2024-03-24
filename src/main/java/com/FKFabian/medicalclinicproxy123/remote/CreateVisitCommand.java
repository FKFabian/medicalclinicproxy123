package com.FKFabian.medicalclinicproxy123.remote;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CreateVisitCommand {
    private final String doctorEmail;
    private final String patientEmail;
    private final VisitCreateDto visitCreateDto;
}
