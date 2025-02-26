package com.salesianos.FitQuestPrototype.Entrenamiento.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio.CreateEjercicioCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.EjercicioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EjercicioService {

    private final EjercicioRepository ejercicioRepository;
    private final NivelService nivelService;

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
        Ejercicio ejercicio = findEjercicioById(id);

        ejercicio.setNombre(createEjercicioCmd.nombre());
        ejercicio.setDescripcion(createEjercicioCmd.descripcion());
        ejercicio.setSeries(createEjercicioCmd.series());
        ejercicio.setRepeticiones(createEjercicioCmd.repeticiones());
        ejercicio.setDuracion(createEjercicioCmd.duracion());
        ejercicio.setUrlImagenes(createEjercicioCmd.urlImagen());

        return ejercicioRepository.save(ejercicio);
    }

    public Ejercicio actualizarNivel (Long id, Long idNivel){
        Ejercicio ejercicio = findEjercicioById(id);

        Nivel nivel = nivelService.findNivelById(idNivel);


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
