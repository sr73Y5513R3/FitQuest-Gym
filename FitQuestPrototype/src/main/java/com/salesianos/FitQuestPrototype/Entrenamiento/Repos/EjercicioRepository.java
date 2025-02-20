package com.salesianos.FitQuestPrototype.Entrenamiento.Repos;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {

    @Query("""
    SELECT e
    FROM Ejercicio e 
""")
    List<Ejercicio> findAllEjercicios();

}
