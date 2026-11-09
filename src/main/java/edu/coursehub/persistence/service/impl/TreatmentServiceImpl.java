package edu.coursehub.persistence.service.impl;

import edu.coursehub.persistence.domain.Animal;
import edu.coursehub.persistence.domain.Specialist;
import edu.coursehub.persistence.domain.Treatment;
import edu.coursehub.persistence.dto.request.CreateTreatmentRequest;
import edu.coursehub.persistence.dto.response.TreatmentResponse;
import edu.coursehub.persistence.exception.ResourceNotFoundException;
import edu.coursehub.persistence.mapper.TreatmentMapper;
import edu.coursehub.persistence.repository.AnimalRepository;
import edu.coursehub.persistence.repository.SpecialistRepository;
import edu.coursehub.persistence.repository.TreatmentRepository;
import edu.coursehub.persistence.service.TreatmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final AnimalRepository animalRepository;
    private final SpecialistRepository specialistRepository;
    private final TreatmentMapper treatmentMapper;

    public TreatmentServiceImpl(TreatmentRepository treatmentRepository,
                                AnimalRepository animalRepository,
                                SpecialistRepository specialistRepository,
                                TreatmentMapper treatmentMapper) {
        this.treatmentRepository = treatmentRepository;
        this.animalRepository = animalRepository;
        this.specialistRepository = specialistRepository;
        this.treatmentMapper = treatmentMapper;
    }

    @Override
    @Transactional
    public TreatmentResponse createTreatment(CreateTreatmentRequest request) {
        Animal animal = animalRepository.findByAnimalCode(request.animalCode())
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found with code: " + request.animalCode()));

        Specialist specialist = specialistRepository.findByProfessionalCode(request.specialistCode())
                .orElseThrow(() -> new ResourceNotFoundException("Specialist not found with code: " + request.specialistCode()));

        Treatment treatment = new Treatment(
                request.performedAt(),
                request.type(),
                request.description()
        );
        treatment.setAnimal(animal);
        treatment.setSpecialist(specialist);

        Treatment savedTreatment = treatmentRepository.save(treatment);
        return treatmentMapper.toResponse(savedTreatment);
    }

    
    @Override
    public List<TreatmentResponse> findByRescueCaseCode(String caseCode) {
        return treatmentRepository.findByRescueCaseCode(caseCode).stream()
                .map(treatmentMapper::toResponse)
                .toList();
    }
}