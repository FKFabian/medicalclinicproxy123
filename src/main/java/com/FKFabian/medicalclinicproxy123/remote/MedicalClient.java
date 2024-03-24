package com.FKFabian.medicalclinicproxy123.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "medicalClient", configuration = ClientConfiguration.class)
public interface MedicalClient {

    @GetMapping("/patients")
    List<PatientDTO> getPatients();

    @GetMapping("/patients/{email}")
    PatientDTO getPatient(@PathVariable("email") String email);

    @GetMapping("/patients/visits")
    List<PatientDTO> getPatientsByVisitDate(@RequestParam("visitDate") LocalDate visitDate);

    @GetMapping("/patients/{email}/visits")
    List<VisitDto> getPatientVisits(@PathVariable("email") String email);

    @PostMapping("/patients")
    PatientDTO addPatient(@RequestBody PatientCreateDto patientCreateDto);

    @PostMapping("/visits/{email}")
    VisitDto addVisit(@PathVariable("email") String email, @RequestBody VisitCreateDto visitCreateDto);

    @RequestMapping(value = "/patients/{email}/visits/{visitId}", method = RequestMethod.PATCH)
    VisitDto assignPatientToVisit(@PathVariable("email") String email, @PathVariable("visitId") Long visitId);
}
