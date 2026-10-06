package edu.coursehub.persistence.service.impl;

import edu.coursehub.persistence.domain.Animal;
import edu.coursehub.persistence.domain.RescueStatus;
import edu.coursehub.persistence.dto.response.AnimalResponse;
import edu.coursehub.persistence.exception.ResourceNotFoundException;
import edu.coursehub.persistence.mapper.AnimalMapper;
import edu.coursehub.persistence.repository.AnimalRepository;
import edu.coursehub.persistence.service.AnimalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository repository;
    private final AnimalMapper mapper;

    public AnimalServiceImpl(AnimalRepository repository, AnimalMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public AnimalResponse findByCode(String animalCode) {
        return repository.findByAnimalCode(animalCode)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found: " + animalCode));
    }

    @Override
public List<AnimalResponse> findAnimalsInRehabilitation() {
    return repository.findByRescueCase_Status(RescueStatus.IN_REHABILITATION)
            .stream()
            .map(mapper::toResponse)
            .toList();
}

    @Override
    public boolean canReceiveTreatment(String animalCode) {
        return repository.findByAnimalCode(animalCode)
                .map(animal -> {
                    RescueStatus status = animal.getRescueCase().getStatus();
                    return status == RescueStatus.UNDER_EVALUATION || status == RescueStatus.IN_REHABILITATION;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found: " + animalCode));
    }
}