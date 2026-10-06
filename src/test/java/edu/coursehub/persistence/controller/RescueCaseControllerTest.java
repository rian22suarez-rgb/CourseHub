package edu.coursehub.persistence.controller;

import edu.coursehub.persistence.domain.RescueStatus;
import edu.coursehub.persistence.dto.request.ChangeRescueStatusRequest;
import edu.coursehub.persistence.dto.response.RescueCaseResponse;
import edu.coursehub.persistence.exception.BusinessRuleException;
import edu.coursehub.persistence.exception.GlobalExceptionHandler;
import edu.coursehub.persistence.exception.ResourceNotFoundException;
import edu.coursehub.persistence.service.RescueCaseService;

// Imports actualizados para Spring Boot 4.1.1
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RescueCaseController.class)
@Import(GlobalExceptionHandler.class)
class RescueCaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RescueCaseService service;

    @Test
    void shouldReturnRescueCaseByCode() throws Exception {
        RescueCaseResponse response = new RescueCaseResponse(
                1L,
                "RES-2026-001",
                LocalDate.of(2026, 8, 20),
                "Bahia Concha",
                RescueStatus.IN_REHABILITATION,
                "DB-CAR",
                "AN-2026-001"
        );

        when(service.findByCode("RES-2026-001")).thenReturn(response);

        mockMvc.perform(get("/api/rescue-cases/{code}", "RES-2026-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.caseCode").value("RES-2026-001"))
                .andExpect(jsonPath("$.status").value("IN_REHABILITATION"));

        verify(service).findByCode("RES-2026-001");
    }

    @Test
    void shouldReturn404WhenCaseDoesNotExist() throws Exception {
        when(service.findByCode("RES-999"))
                .thenThrow(new ResourceNotFoundException("Rescue case not found: RES-999"));

        mockMvc.perform(get("/api/rescue-cases/{code}", "RES-999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Rescue case not found: RES-999"));
    }

    @Test
    void shouldReturnCasesByStatus() throws Exception {
        RescueCaseResponse case1 = new RescueCaseResponse(
                1L, "RES-001", LocalDate.of(2026, 8, 20), "Bahia Concha",
                RescueStatus.IN_REHABILITATION, "DB-CAR", "AN-001"
        );
        RescueCaseResponse case2 = new RescueCaseResponse(
                2L, "RES-002", LocalDate.of(2026, 8, 21), "Playa Blanca",
                RescueStatus.IN_REHABILITATION, "DB-CAR", "AN-002"
        );

        when(service.findByStatus(RescueStatus.IN_REHABILITATION))
                .thenReturn(List.of(case1, case2));

        mockMvc.perform(get("/api/rescue-cases?status=IN_REHABILITATION"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].caseCode").value("RES-001"))
                .andExpect(jsonPath("$[1].caseCode").value("RES-002"));
    }

    @Test
    void shouldReturn400WhenStatusIsInvalid() throws Exception {
        mockMvc.perform(get("/api/rescue-cases?status=FLYING"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid request parameter"));
    }

    @Test
    void shouldChangeStatusSuccessfully() throws Exception {
        RescueCaseResponse response = new RescueCaseResponse(
                1L, "RES-001", LocalDate.of(2026, 8, 20), "Bahia Concha",
                RescueStatus.READY_FOR_RELEASE, "DB-CAR", "AN-001"
        );

        when(service.changeStatus(eq("RES-001"), any(ChangeRescueStatusRequest.class)))
                .thenReturn(response);

        String jsonRequest = """
                {
                "status": "READY_FOR_RELEASE"
                }
                """;

        mockMvc.perform(patch("/api/rescue-cases/{code}/status", "RES-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("READY_FOR_RELEASE"));

        verify(service).changeStatus(eq("RES-001"), any(ChangeRescueStatusRequest.class));
        }

        @Test
        void shouldReturn400WhenChangeStatusRequestIsInvalid() throws Exception {
        String jsonRequest = """
                {
                "status": null
                }
                """;

        mockMvc.perform(patch("/api/rescue-cases/{code}/status", "RES-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.details.status").value("Status is required"));

        verify(service, never()).changeStatus(anyString(), any());
        }

        @Test
        void shouldReturn409WhenTransitionIsInvalid() throws Exception {
        when(service.changeStatus(eq("RES-001"), any(ChangeRescueStatusRequest.class)))
                .thenThrow(new BusinessRuleException("Invalid status transition"));

        String jsonRequest = """
                {
                "status": "READY_FOR_RELEASE"
                }
                """;

        mockMvc.perform(patch("/api/rescue-cases/{code}/status", "RES-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Invalid status transition"));
        }
}