package com.salesianos.FitQuestPrototype.Entrenamiento.Repos;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

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
    Optional<Ejercicio> findEjercicioById(@PathVariable("id") Long id);

    @Query("""
    SELECT e
    FROM Ejercicio e
    WHERE e.nombre = :nombre
""")
    Optional<Ejercicio> findEjercicioByNombre(@PathVariable("nombre") String nombre);

}
