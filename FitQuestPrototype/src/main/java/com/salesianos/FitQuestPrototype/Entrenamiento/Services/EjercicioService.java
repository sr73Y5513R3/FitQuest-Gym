package com.salesianos.FitQuestPrototype.Entrenamiento.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio.CreateEjercicioCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.EjercicioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EjercicioService {

    private final EjercicioRepository ejercicioRepository;

    public List<Ejercicio> findAllEjercicio(){
        return ejercicioRepository.findAllEjercicios();
    }

    public Ejercicio findEjercicioById(Long id){
        Optional<Ejercicio> ejercicio = ejercicioRepository.findById(id);

        if (ejercicio.isEmpty())
            throw new EntityNotFoundException("Ejercicio no encontrado con ese id");

        return ejercicio.get();
    }

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
