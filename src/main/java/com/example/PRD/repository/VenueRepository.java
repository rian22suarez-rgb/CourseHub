package com.example.PRD.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.PRD.model.Venue;

import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    
    // Query Method para buscar un Venue por su código de negocio único
    Optional<Venue> findByCode(String code);
}
