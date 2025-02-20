package com.salesianos.FitQuestPrototype.Entrenamiento.Repos;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {

    @Query("""
    SELECT e
    FROM Ejercicio e 
""")
    List<Ejercicio> findAllEjercicios();

    @Query("""
    SELECT e
    FROM Ejercicio e
    WHERE e.id = :id
""")
    Optional<Ejercicio> findEjercicioById(Long id);

}
