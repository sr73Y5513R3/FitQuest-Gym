package com.salesianos.FitQuestPrototype.Entrenamiento.Repos;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {
}
