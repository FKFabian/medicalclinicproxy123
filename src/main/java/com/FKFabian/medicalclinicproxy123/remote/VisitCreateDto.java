package com.FKFabian.medicalclinicproxy123.remote;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitCreateDto {
    private final LocalDateTime startingVisitDate;
    private final LocalDateTime endingVisitDate;
}
