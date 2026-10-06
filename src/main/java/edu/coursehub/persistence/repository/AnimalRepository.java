package edu.coursehub.persistence.repository;

import edu.coursehub.persistence.domain.Animal;
import edu.coursehub.persistence.domain.RescueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Optional<Animal> findByAnimalCode(String animalCode);

    // Agrega este método para buscar por el estado del RescueCase asociado
    List<Animal> findByRescueCase_Status(RescueStatus status);
}