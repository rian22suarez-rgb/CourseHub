package edu.coursehub.persistence.controller;

import edu.coursehub.persistence.dto.response.AnimalResponse;
import edu.coursehub.persistence.dto.response.TreatmentEligibilityResponse;
import edu.coursehub.persistence.dto.response.TreatmentResponse;
import edu.coursehub.persistence.service.AnimalService;
import edu.coursehub.persistence.service.TreatmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animals")
public class AnimalController {

    private final AnimalService animalService;
    private final TreatmentService treatmentService;

    public AnimalController(AnimalService animalService, TreatmentService treatmentService) {
        this.animalService = animalService;
        this.treatmentService = treatmentService;
    }

    @GetMapping("/{animalCode}")
    public ResponseEntity<AnimalResponse> findByCode(@PathVariable String animalCode) {
        return ResponseEntity.ok(animalService.findByCode(animalCode));
    }

    @GetMapping("/in-rehabilitation")
    public ResponseEntity<List<AnimalResponse>> findAnimalsInRehabilitation() {
        return ResponseEntity.ok(animalService.findAnimalsInRehabilitation());
    }

    @GetMapping("/{animalCode}/treatments")
    public ResponseEntity<List<TreatmentResponse>> findTreatments(@PathVariable String animalCode) {
        return ResponseEntity.ok(treatmentService.findByRescueCaseCode(animalCode));
    }

    @GetMapping("/{animalCode}/treatment-eligibility")
    public ResponseEntity<TreatmentEligibilityResponse> canReceiveTreatment(@PathVariable String animalCode) {
        boolean eligible = animalService.canReceiveTreatment(animalCode);
        return ResponseEntity.ok(new TreatmentEligibilityResponse(animalCode, eligible));
    }
}