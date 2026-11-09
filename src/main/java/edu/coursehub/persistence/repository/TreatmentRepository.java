package edu.coursehub.persistence.repository;

import edu.coursehub.persistence.domain.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    @Query("SELECT t FROM Treatment t WHERE t.animal.rescueCase.caseCode = :caseCode")
    List<Treatment> findByRescueCaseCode(@Param("caseCode") String caseCode);
    
}