package com.FKFabian.medicalclinicproxy123.remote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 8888)
public class MedicalClientTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MedicalClient medicalClient;
    @Autowired
    WireMockServer wireMockServer;

    @Test
    void getPatients_DataCorrect_ReturnPatientsDto() throws JsonProcessingException {
        List<PatientDTO> patients = new ArrayList<>();
        patients.add(PatientDTO.builder()
                .email("example@gmail.com")
                .idCardNo("123A")
                .firstName("John")
                .build());
        patients.add(PatientDTO.builder()
                .email("example111@gmail.com")
                .idCardNo("321A")
                .firstName("Leon")
                .build());
        wireMockServer.stubFor(get(urlEqualTo("/patients"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(patients))));

        var result = medicalClient.getPatients();

        assertNotNull(result);
        assertEquals("example@gmail.com", result.get(0).getEmail());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("example111@gmail.com", result.get(1).getEmail());
        assertEquals("Leon", result.get(1).getFirstName());
    }

    @Test
    void getPatient_CorrectData_ReturnPatientDto() throws JsonProcessingException {
        PatientDTO patient = PatientDTO.builder()
                .firstName("John")
                .email("example@com")
                .idCardNo("123A")
                .build();
        wireMockServer.stubFor(get(urlEqualTo("/patients/example%40com"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(patient))));

        var result = medicalClient.getPatient("example@com");

        assertNotNull(result);
        assertEquals("example@com", result.getEmail());
        assertEquals("John", result.getFirstName());
        assertEquals("123A", result.getIdCardNo());
    }

    @Test
    void getPatientVisits_Correct_Data_ReturnVisitsDto() throws JsonProcessingException {
        String patientsEmail = "example@com";
        PatientDTO patient = new PatientDTO(patientsEmail,
                "123A", "John",
                "Smith", "200-111",
                LocalDate.of(2000, 12, 12), new ArrayList<>());
        LocalDateTime startingVisitTime = LocalDateTime
                .of(2028, 10, 15, 15, 0);
        LocalDateTime endingVisitTime = LocalDateTime
                .of(2028, 10, 15, 15, 30);
        patient.getVisits()
                .add(new VisitDto(1L, startingVisitTime, endingVisitTime, 10L, 15L));

        wireMockServer.stubFor(get(urlEqualTo("/patients/example%40com/visits"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(patient.getVisits()))));

        var result = medicalClient.getPatientVisits(patientsEmail);

        assertNotNull(result);
        assertEquals(10L, result.get(0).getPatientId());
        assertEquals(15L, result.get(0).getDoctorId());
        assertEquals(startingVisitTime, result.get(0).getStartingVisitDate());
    }

    @Test
    void addPatient_CorrectData_ReturnPatientDto() throws JsonProcessingException {
        PatientCreateDto patientCreateDto = new PatientCreateDto("example@com", "password",
                "123A", "John", "Smith",
                "200-111", LocalDate.of(2000, 12, 12));
        PatientDTO patient = PatientDTO.builder()
                .firstName("John")
                .email("example@com")
                .idCardNo("123A")
                .build();
        wireMockServer.stubFor(post(urlEqualTo("/patients"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(patient))));

        var result = medicalClient.addPatient(patientCreateDto);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("123A", result.getIdCardNo());
        assertEquals("example@com", result.getEmail());
    }

    @Test
    void addVisit_CorrectData_ReturnVisitDto() throws JsonProcessingException {
        String doctorEmail = "doctorEmail@com";
        LocalDateTime startingVisitTime = LocalDateTime
                .of(2027, 10, 15, 15, 0);
        LocalDateTime endingVisitTime = LocalDateTime
                .of(2027, 10, 15, 15, 30);
        VisitCreateDto visitCreateDto = new VisitCreateDto(startingVisitTime, endingVisitTime);
        VisitDto visit = new VisitDto(1L, startingVisitTime, endingVisitTime, null, 15L);
        wireMockServer.stubFor(post(urlEqualTo("/visits/doctorEmail%40com"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(visit))));

        var result = medicalClient.addVisit(doctorEmail, visitCreateDto);

        assertNotNull(result);
        assertEquals(startingVisitTime, result.getStartingVisitDate());
        assertEquals(endingVisitTime, result.getEndingVisitDate());
        assertEquals(15L, result.getDoctorId());
    }

    @Test
    void assignPatientToVisit_CorrectData_ReturnVisitDto() throws JsonProcessingException {
        String patientsMail = "patientsMail@com";
        Long visitId = 1L;
        LocalDateTime startingVisitTime = LocalDateTime
                .of(2026, 10, 15, 15, 0);
        LocalDateTime endingVisitTime = LocalDateTime
                .of(2026, 10, 15, 15, 30);
        VisitDto visit = new VisitDto(visitId, startingVisitTime, endingVisitTime, 10L, 15L);
        wireMockServer.stubFor(patch(urlEqualTo("/patients/patientsMail%40com/visits/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(visit))));

        var result = medicalClient.assignPatientToVisit(patientsMail, visitId);

        assertNotNull(result);
        assertEquals(10L, result.getPatientId());
        assertEquals(15L, result.getDoctorId());
        assertEquals(startingVisitTime, result.getStartingVisitDate());
        assertEquals(endingVisitTime, result.getEndingVisitDate());
    }
}
