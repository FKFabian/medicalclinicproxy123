package com.FKFabian.medicalclinicproxy123.controller;

import com.FKFabian.medicalclinicproxy123.remote.*;
import com.FKFabian.medicalclinicproxy123.service.ProxyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProxyControllerTest {
    @MockBean
    ProxyService proxyService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getPatients_DataCorrect_ReturnPatientsDto() throws Exception {
        when(proxyService.getPatients(any())).thenReturn(List.of(patientDto1(), patientDto2()));
        mockMvc.perform(get("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jan"));
    }

    @Test
    void getPatientsWithVisitDate_DataCorrect_ReturnPatientsDto() throws Exception {
        LocalDate localDate = LocalDate.now();

        when(proxyService.getPatients(localDate)).thenReturn(List.of(patientDto1(), patientDto2()));
        mockMvc.perform(get("/api/patients?visitDate={localDate}", localDate)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jan"));
    }

    @Test
    void getPatient_DataCorrect_ReturnPatientDto() throws Exception {
        String email = "patient1@com";

        when(proxyService.getPatient(email)).thenReturn(patientDto1());
        mockMvc.perform(get("/api/patients/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("patient1@com"))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void getPatientVisits_DataCorrect_ReturnVisitsDto() throws Exception {
        String email = "paetient1@com";

        when(proxyService.getPatientVisits(email)).thenReturn(List.of(visitDto1(), visitDto2()));
        mockMvc.perform(get("/api/patients/{email}/visits", email)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].doctorId").value(15L))
                .andExpect(jsonPath("$[1].patientId").value(1L));
    }

    @Test
    void addPatient_DataCorrect_ReturnPatientDto() throws Exception {
        when(proxyService.addPatient(patientCreateDto1())).thenReturn(patientDto1());
        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(patientCreateDto1()))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("patient1@com"));
    }

    @Test
    void addVisit_DataCorrect_ReturnVisitDto() throws Exception {
        when(proxyService.addVisit(createVisitCommand())).thenReturn(visitDto1());
        mockMvc.perform(post("/api/visits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(createVisitCommand()))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.doctorId").value(15L));
//                .andExpect(jsonPath("$.startingVisitDate").value(LocalDateTime
//                        .of(2027, 12, 12, 15, 0,0)));
    }

    private static PatientDTO patientDto1() {
        return new PatientDTO("patient1@com", "1A",
                "John", "Smith", "200-111",
                LocalDate.of(2000, 12, 12), new ArrayList<>());
    }

    private static PatientDTO patientDto2() {
        return new PatientDTO("patient2@com", "2A",
                "Jan", "Nowak", "300-111",
                LocalDate.of(2001, 11, 11), new ArrayList<>());
    }

    private static PatientCreateDto patientCreateDto1() {
        return new PatientCreateDto("patient1@com", "pass", "1A"
                , "John", "Smith"
                , "200-111", LocalDate.of(2000, 12, 12));
    }

    private static VisitDto visitDto1() {
        LocalDateTime startingVisitTime = LocalDateTime
                .of(2027, 12, 12, 15, 0);
        LocalDateTime endingVisitTime = LocalDateTime
                .of(2027, 12, 12, 15, 30);
        return new VisitDto(1L, startingVisitTime, endingVisitTime, 10L, 15L);
    }

    private static VisitDto visitDto2() {
        LocalDateTime startingVisitTime = LocalDateTime
                .of(2027, 12, 15, 14, 0);
        LocalDateTime endingVisitTime = LocalDateTime
                .of(2027, 12, 15, 14, 30);
        return new VisitDto(2L, startingVisitTime, endingVisitTime, 1L, 2L);
    }

    private static VisitCreateDto visitCreateDto() {
        LocalDateTime startingVisitTime = LocalDateTime
                .of(2027, 12, 12, 15, 0);
        LocalDateTime endingVisitTime = LocalDateTime
                .of(2027, 12, 12, 15, 30);
        return new VisitCreateDto(startingVisitTime, endingVisitTime);
    }

    private static CreateVisitCommand createVisitCommand() {
        return new CreateVisitCommand("doctor1@com", "patient1@com", visitCreateDto());
    }
}
