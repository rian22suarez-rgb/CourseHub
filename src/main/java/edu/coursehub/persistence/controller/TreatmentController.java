package edu.coursehub.persistence.controller;

import edu.coursehub.persistence.dto.request.CreateTreatmentRequest;
import edu.coursehub.persistence.dto.response.TreatmentResponse;
import edu.coursehub.persistence.service.TreatmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatments")
public class TreatmentController {

    private final TreatmentService service;

    public TreatmentController(TreatmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TreatmentResponse> createTreatment(@Valid @RequestBody CreateTreatmentRequest request) {
        TreatmentResponse response = service.createTreatment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/rescue-case/{caseCode}")
    public ResponseEntity<List<TreatmentResponse>> findByRescueCaseCode(@PathVariable String caseCode) {
        return ResponseEntity.ok(service.findByRescueCaseCode(caseCode));
    }
}