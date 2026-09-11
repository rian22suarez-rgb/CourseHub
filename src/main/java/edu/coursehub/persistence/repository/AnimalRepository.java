package edu.coursehub.persistence.repository;

import edu.coursehub.persistence.domain.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Optional<Animal> findByAnimalCode(String animalCode);
}