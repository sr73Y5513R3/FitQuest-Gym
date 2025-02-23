package com.salesianos.FitQuestPrototype.Entrenamiento.Repos;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface NivelRepository extends JpaRepository<Nivel, Long> {

    @Query ("""
    SELECT n
    FROM Nivel n LEFT JOIN FETCH n.entrenamientos
""")
    List<Nivel> findAllNiveles ();

    @Query("""
    SELECT n
    FROM Nivel n LEFT JOIN FETCH n.entrenamientos
    WHERE n.id = :id
""")
    Optional<Nivel> findNivelById(@PathVariable("id") Long id);

    @Query("""
    SELECT n
    FROM Nivel n LEFT JOIN FETCH n.entrenamientos
    WHERE n.nombre = :nombre
""")
    Optional<Nivel> findNivelByNombre(@PathVariable("nombre") String nombre);
}
