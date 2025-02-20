package com.salesianos.FitQuestPrototype.Entrenamiento.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio.CreateEjercicioCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.EjercicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EjercicioService {

    private final EjercicioRepository ejercicioRepository;

    public Ejercicio save (CreateEjercicioCmd createEjercicioCmd){
        return ejercicioRepository.save(Ejercicio.builder()
                .nombre(createEjercicioCmd.nombre())
                .descripcion(createEjercicioCmd.descripcion())
                .series(createEjercicioCmd.series())
                .repeticiones(createEjercicioCmd.repeticiones())
                .duracion(createEjercicioCmd.duracion())
                .urlImagenes(createEjercicioCmd.urlImagen())
                .build());
    }

}
