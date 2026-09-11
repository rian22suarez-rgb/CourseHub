package edu.coursehub.persistence.repository;

import edu.coursehub.persistence.domain.RescueCase;
import edu.coursehub.persistence.domain.RescueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RescueCaseRepository extends JpaRepository<RescueCase, Long> {
    Optional<RescueCase> findByCaseCode(String caseCode);
    List<RescueCase> findByStatus(RescueStatus status);
}