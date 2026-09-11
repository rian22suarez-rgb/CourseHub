package edu.coursehub.persistence.repository;

import edu.coursehub.persistence.domain.RescueCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RescueCenterRepository extends JpaRepository<RescueCenter, Long> {
    Optional<RescueCenter> findByCode(String code);
}