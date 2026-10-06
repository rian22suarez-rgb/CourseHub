package edu.coursehub.persistence.controller;

import edu.coursehub.persistence.domain.RescueStatus;
import edu.coursehub.persistence.dto.request.ChangeRescueStatusRequest;
import edu.coursehub.persistence.dto.response.RescueCaseResponse;
import edu.coursehub.persistence.service.RescueCaseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rescue-cases")
public class RescueCaseController {

    private final RescueCaseService service;

    public RescueCaseController(RescueCaseService service) {
        this.service = service;
    }

    @GetMapping("/{caseCode}")
    public ResponseEntity<RescueCaseResponse> findByCode(@PathVariable String caseCode) {
        return ResponseEntity.ok(service.findByCode(caseCode));
    }

    @GetMapping
    public ResponseEntity<List<RescueCaseResponse>> findByStatus(@RequestParam RescueStatus status) {
        return ResponseEntity.ok(service.findByStatus(status));
    }

    @PatchMapping("/{caseCode}/status")
    public ResponseEntity<RescueCaseResponse> changeStatus(
            @PathVariable String caseCode,
            @Valid @RequestBody ChangeRescueStatusRequest request) {
        return ResponseEntity.ok(service.changeStatus(caseCode, request));
    }
}