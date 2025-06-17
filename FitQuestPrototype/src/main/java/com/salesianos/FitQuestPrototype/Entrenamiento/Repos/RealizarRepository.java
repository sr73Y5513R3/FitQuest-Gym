package com.salesianos.FitQuestPrototype.Entrenamiento.Repos;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Realiza;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.RealizaID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface RealizarRepository extends JpaRepository<Realiza, RealizaID> {

    @Query("""
            SELECT r
            FROM Realiza r
            Where r.realizado = false
            """)
    Page<Realiza> findAllSinAceptar(Pageable pageable);

    @Query("""
            SELECT r
            FROM Realiza r
            Where r.realizado = true
            """)
    Page<Realiza> findAllAceptados(Pageable pageable);

    @Query("""
            SELECT r
            FROM Realiza r
            Where r.usuario.id = :idUsuario
            """)
    Page<Realiza> findAllByUser(@Param("idUsuario") UUID idUsuario, Pageable pageable);
}
