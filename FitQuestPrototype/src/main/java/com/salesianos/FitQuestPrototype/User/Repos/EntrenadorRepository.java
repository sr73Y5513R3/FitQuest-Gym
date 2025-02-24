package com.salesianos.FitQuestPrototype.User.Repos;

import com.salesianos.FitQuestPrototype.User.Model.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EntrenadorRepository extends JpaRepository<Entrenador, UUID> {

    @Query("""
            SELECT e 
            FROM Entrenador e LEFT JOIN FETCH e.entrenamientos
            """)
    List<Entrenador> findAllEntrenador();

}
