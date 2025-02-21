package com.salesianos.FitQuestPrototype.Entrenamiento.Repos;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NivelRepository extends JpaRepository<Nivel, Long> {

    @Query ("""
    SELECT n
    FROM Nivel n LEFT JOIN FETCH n.entrenamientos
""")
    List<Nivel> findAllNiveles ();
}
