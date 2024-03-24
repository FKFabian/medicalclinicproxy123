package com.FKFabian.medicalclinicproxy123.controller;

import com.FKFabian.medicalclinicproxy123.remote.CreateVisitCommand;
import com.FKFabian.medicalclinicproxy123.remote.PatientCreateDto;
import com.FKFabian.medicalclinicproxy123.remote.PatientDTO;
import com.FKFabian.medicalclinicproxy123.remote.VisitDto;
import com.FKFabian.medicalclinicproxy123.service.ProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProxyController {
    private final ProxyService proxyService;

    @GetMapping("/patients")
    List<PatientDTO> getPatients(@RequestParam(required = false) LocalDate visitDate) {
        return proxyService.getPatients(visitDate);
    }

    @GetMapping("/patients/{email}")
    PatientDTO getPatient(@PathVariable("email") String email) {
        return proxyService.getPatient(email);
    }

    @GetMapping("/patients/{email}/visits")
    List<VisitDto> getPatientVisits(@PathVariable("email") String email) {
        return proxyService.getPatientVisits(email);
    }

    @PostMapping("/patients")
    PatientDTO addPatient(@RequestBody PatientCreateDto patientCreateDto) {
        return proxyService.addPatient(patientCreateDto);
    }

    @PostMapping("/visits")
    public VisitDto addVisit(@RequestBody CreateVisitCommand addVisitRequest) {
        return proxyService.addVisit(addVisitRequest);
    }
}
