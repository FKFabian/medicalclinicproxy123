package com.FKFabian.medicalclinicproxy123.remote;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitDto {
    private final Long id;
    private final LocalDateTime startingVisitDate;
    private final LocalDateTime endingVisitDate;
    private final Long patientId;
    private final Long doctorId;
}
