package com.salesianos.FitQuestPrototype.Entrenamiento.Repos;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EntrenamientoRepository extends JpaRepository<Entrenamiento, Long> {

    @Query("""
    SELECT e 
    FROM Entrenamiento e LEFT JOIN FETCH e.ejercicios ej 
""")
    List<Entrenamiento> findAllEntrenamientos();

    @Query("""
    SELECT e
    FROM Entrenamiento e LEFT JOIN FETCH e.ejercicios ej
    WHERE e.id = :id
""")
    Optional<Entrenamiento> findEntrenamientoById(Long id);
}
