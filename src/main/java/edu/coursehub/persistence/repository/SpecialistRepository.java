package edu.coursehub.persistence.repository;

import edu.coursehub.persistence.domain.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialist, Long> {
    Optional<Specialist> findByProfessionalCode(String professionalCode);
    Optional<Specialist> findByEmail(String email);
}