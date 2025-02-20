package com.salesianos.FitQuestPrototype.User.Repos;

import com.salesianos.FitQuestPrototype.User.Model.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EntrenadorRepository extends JpaRepository<Entrenador, UUID> {
}
