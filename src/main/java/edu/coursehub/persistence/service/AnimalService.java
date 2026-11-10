package edu.coursehub.persistence.service;

import edu.coursehub.persistence.dto.response.AnimalResponse;
import java.util.List;

public interface AnimalService {
    AnimalResponse findByCode(String animalCode);
    List<AnimalResponse> findAnimalsInRehabilitation();
    boolean canReceiveTreatment(String animalCode);
}