package com.FKFabian.medicalclinicproxy123.service;

import com.FKFabian.medicalclinicproxy123.remote.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class ProxyServiceTest {
    ProxyService proxyService;
    MedicalClient medicalClient;

    @BeforeEach
    void init() {
        this.medicalClient = Mockito.mock(MedicalClient.class);
        this.proxyService = new ProxyService(medicalClient);
    }

    @Test
    void getPatients_DataCorrect_ReturnPatientsDto() {
        // given
        PatientDTO patient1 = new PatientDTO("patient1@com", "1A", "John",
                "Smith", "200-111", LocalDate
                .of(2000, 12, 12), new ArrayList<>());
        PatientDTO patient2 = new PatientDTO("patient2@com", "2A", "Tom",
                "Jones", "300-111", LocalDate
                .of(2002, 10, 10), new ArrayList<>());
        List<PatientDTO> patients = Arrays.asList(patient1, patient2);
        when(medicalClient.getPatients()).thenReturn(patients);
        // when
        var result = proxyService.getPatients(null);
        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void getPatient_DataCorrect_ReturnPatientDto() {
        //given
        String email = "patient1@com";
        PatientDTO patient1 = new PatientDTO("patient1@com", "1A", "John",
                "Smith", "200-111", LocalDate
                .of(2000, 12, 12), new ArrayList<>());
        when(medicalClient.getPatient(email)).thenReturn(patient1);
        //when
        var result = proxyService.getPatient(email);
        //then
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("patient1@com", result.getEmail());
        assertEquals("Smith", result.getLastName());
    }

    @Test
    void getPatientVisits_DataCorrect_ReturnVisitsDto() {
        //given
        String email = "patient1@com";
        VisitDto visit1 = new VisitDto(1L, LocalDateTime
                .of(2027, 12, 12, 15, 0), LocalDateTime
                .of(2027, 12, 12, 15, 30), 10L, 15L);
        VisitDto visit2 = new VisitDto(2L, LocalDateTime
                .of(2028, 12, 12, 14, 0), LocalDateTime
                .of(2028, 12, 12, 14, 30), 1L, 5L);
        List<VisitDto> visits = List.of(visit1, visit2);
        when(medicalClient.getPatientVisits(email)).thenReturn(visits);
        //when
        var result = proxyService.getPatientVisits(email);
        //then
        assertNotNull(result);
        assertEquals(1L, result.get(1).getPatientId());
        assertEquals(10L, result.get(0).getPatientId());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void addPatient_DataCorrect_ReturnPatientDto() {
        //given
        PatientCreateDto patientCreateDto = new PatientCreateDto("patient1@com", "password", "1A",
                "John", "Smith", "200-111", LocalDate
                .of(2000, 12, 12));
        PatientDTO patient1 = new PatientDTO("patient1@com", "1A", "John",
                "Smith", "200-111", LocalDate
                .of(2000, 12, 12), new ArrayList<>());
        when(medicalClient.addPatient(patientCreateDto)).thenReturn(patient1);
        //when
        var result = proxyService.addPatient(patientCreateDto);
        //then
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("200-111", result.getPhoneNumber());
        assertEquals("patient1@com", result.getEmail());
    }

    @Test
    void addVisit_DataCorrect_ReturnVisitDto() {
        //given
        String doctorEmail = "doctor1@com";
        String patientEmail = "patient1@com";
        VisitCreateDto visitCreateDto = new VisitCreateDto(LocalDateTime
                .of(2027, 12, 12, 15, 0), LocalDateTime
                .of(2027, 12, 12, 15, 30));
        VisitDto visitWithoutPatient = new VisitDto(1L, LocalDateTime
                .of(2027, 12, 12, 15, 0), LocalDateTime
                .of(2027, 12, 12, 15, 30), null, 15L);
        VisitDto visitWithPatient = new VisitDto(1L, LocalDateTime
                .of(2027, 12, 12, 15, 0), LocalDateTime
                .of(2027, 12, 12, 15, 30), 10L, 15L);
        CreateVisitCommand createVisitCommand = new CreateVisitCommand(doctorEmail, patientEmail, visitCreateDto);
        when(medicalClient.addVisit(doctorEmail, visitCreateDto)).thenReturn(visitWithoutPatient);
        when(medicalClient.assignPatientToVisit(patientEmail, visitWithoutPatient.getId())).thenReturn(visitWithPatient);
        //when
        var result = proxyService.addVisit(createVisitCommand);
        //then
        assertNotNull(result);
        assertEquals(15L, result.getDoctorId());
        assertEquals(10L, result.getPatientId());
        assertEquals(1L, result.getId());
    }
}
