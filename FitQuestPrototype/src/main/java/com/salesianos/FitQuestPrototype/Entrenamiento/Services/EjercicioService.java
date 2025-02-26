package com.salesianos.FitQuestPrototype.Entrenamiento.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio.CreateEjercicioCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.EjercicioRepository;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.NivelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EjercicioService {

    private final EjercicioRepository ejercicioRepository;
    private final NivelRepository nivelRepository;

    public Page<Ejercicio> findAllEjercicio(Pageable pageable){
        return ejercicioRepository.findAllEjercicios(pageable);
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

    public Ejercicio edit (Long id, CreateEjercicioCmd createEjercicioCmd){
        Optional<Ejercicio> ejercicioOpt = ejercicioRepository.findById(id);

        if (ejercicioOpt.isEmpty())
            throw new EntityNotFoundException("Ejercicio no encontrado con ese id");

        Ejercicio ejercicio = ejercicioOpt.get();

        ejercicio.setNombre(createEjercicioCmd.nombre());
        ejercicio.setDescripcion(createEjercicioCmd.descripcion());
        ejercicio.setSeries(createEjercicioCmd.series());
        ejercicio.setRepeticiones(createEjercicioCmd.repeticiones());
        ejercicio.setDuracion(createEjercicioCmd.duracion());
        ejercicio.setUrlImagenes(createEjercicioCmd.urlImagen());

        return ejercicioRepository.save(ejercicio);
    }

    public Ejercicio actualizarNivel (Long id, Long idNivel){
        Optional<Ejercicio> ejercicioOpt = ejercicioRepository.findEjercicioById(id);

        Optional<Nivel> nivelOpt = nivelRepository.findNivelById(idNivel);

        if (ejercicioOpt.isEmpty() || nivelOpt.isEmpty())
            throw new EntityNotFoundException("Entidades no encontradas");

        Nivel nivel = nivelOpt.get();
        Ejercicio ejercicio = ejercicioOpt.get();

        nivel.addEjercicio(ejercicio);

        return ejercicioRepository.save(ejercicio);


    }

    public Ejercicio findByNombre(String nombre){
        Optional<Ejercicio> ejercicioOpt = ejercicioRepository.findEjercicioByNombre(nombre);

        if (ejercicioOpt.isEmpty())
            throw new EntityNotFoundException("Ejercicio no encontrado con ese nombre");

        return ejercicioOpt.get();
    }

}
