package com.example.PRD.repository;

import com.example.PRD.model.Artist;
import com.example.PRD.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    // Query Method para buscar artista por nombre artístico único
    Optional<Artist> findByStageName(String stageName);

    // FR-ART-004: eventos donde participa un artista (JPQL con JOIN)
    // FR-ART-004: eventos donde participa un artista (JPQL con JOIN)
@Query("SELECT DISTINCT e FROM Event e JOIN e.artists a WHERE a.stageName = :stageName")
List<Event> findEventsByArtistStageName(@Param("stageName") String stageName);
}