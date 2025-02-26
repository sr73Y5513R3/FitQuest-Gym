package com.salesianos.FitQuestPrototype.Entrenamiento.Repos;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Realiza;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.RealizaID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealizarRepository extends JpaRepository<Realiza, RealizaID> {
}
