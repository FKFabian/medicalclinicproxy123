package com.FKFabian.medicalclinicproxy123.service;

import com.FKFabian.medicalclinicproxy123.remote.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProxyService {
    @Autowired
    private final MedicalClient medicalClient;

    public List<PatientDTO> getPatients(LocalDate visitDate) {
        return visitDate == null ? medicalClient.getPatients() : medicalClient.getPatientsByVisitDate(visitDate);
    }

    public PatientDTO getPatient(String email) {
        return medicalClient.getPatient(email);
    }

    public List<VisitDto> getPatientVisits(String email) {
        return medicalClient.getPatientVisits(email);
    }

    public PatientDTO addPatient(PatientCreateDto patientCreateDto) {
        return medicalClient.addPatient(patientCreateDto);
    }

    public VisitDto addVisit(CreateVisitCommand createVisitCommand) {
        String doctorEmail = createVisitCommand.getDoctorEmail();
        VisitCreateDto visitCreateDto = createVisitCommand.getVisitCreateDto();
        VisitDto visitDTO = medicalClient.addVisit(doctorEmail, visitCreateDto);
        String patientEmail = createVisitCommand.getPatientEmail();
        return medicalClient.assignPatientToVisit(patientEmail, visitDTO.getId());
    }
}
