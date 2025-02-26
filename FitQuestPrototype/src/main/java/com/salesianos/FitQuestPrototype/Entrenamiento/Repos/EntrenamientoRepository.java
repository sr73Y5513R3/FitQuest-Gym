package com.salesianos.FitQuestPrototype.Entrenamiento.Repos;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface EntrenamientoRepository extends JpaRepository<Entrenamiento, Long> {

    @Query("""
    SELECT e 
    FROM Entrenamiento e LEFT JOIN FETCH e.ejercicios ej 
""")
    Page<Entrenamiento> findAllEntrenamientos(Pageable pageable);

    @Query("""
    SELECT e
    FROM Entrenamiento e LEFT JOIN FETCH e.ejercicios ej
    WHERE e.id = :id
""")
    Optional<Entrenamiento> findEntrenamientoById(@PathVariable("id") Long id);

    @Query("""
    SELECT e
    FROM Entrenamiento e LEFT JOIN FETCH e.ejercicios ej
    WHERE e.nombre = :nombre
""")
    Optional<Entrenamiento> findEntrenamientoByNombre(@Param("nombre") String nombre);

}
