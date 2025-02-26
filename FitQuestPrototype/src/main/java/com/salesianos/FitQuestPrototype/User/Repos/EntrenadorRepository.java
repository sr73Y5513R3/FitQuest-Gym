package com.salesianos.FitQuestPrototype.User.Repos;

import com.salesianos.FitQuestPrototype.User.Model.Entrenador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntrenadorRepository extends JpaRepository<Entrenador, UUID> {

    @Query("""
            SELECT e 
            FROM Entrenador e LEFT JOIN FETCH e.entrenamientos
            """)
    Page<Entrenador> findAllEntrenador(Pageable pageable);

    @Query("""
    SELECT e
    FROM Entrenador e LEFT JOIN FETCH e.entrenamientos
    WHERE e.id= :id
""")
    Optional<Entrenador> findEntrenadorById(@PathVariable("id")UUID id);

}
