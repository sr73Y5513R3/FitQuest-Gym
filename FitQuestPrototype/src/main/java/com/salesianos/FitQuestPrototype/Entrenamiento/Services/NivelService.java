package com.salesianos.FitQuestPrototype.Entrenamiento.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Nivel.CreateNivelCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Error.EntidadNoEncontradaException;
import com.salesianos.FitQuestPrototype.Entrenamiento.Error.EntidadYaAñadidaException;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.EntrenamientoRepository;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.NivelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NivelService {

    private final NivelRepository nivelRepository;
    private final EntrenamientoRepository entrenamientoRepository;
    private final EjercicioService ejercicioService;

    public Page<Nivel> findAllNiveles(Pageable pageable) {
        return nivelRepository.findAllNiveles(pageable);
    }

    public Nivel findNivelById(Long id){
        Optional<Nivel> nivel = nivelRepository.findById(id);

        if(nivel.isEmpty())
            throw new EntidadNoEncontradaException("Nivel no encontrado con ese id");

        return nivel.get();

    }

    public Nivel save (CreateNivelCmd newNivel){
        return nivelRepository.save(Nivel.builder()
                .nombre(newNivel.nombre())
                .build());
    }

    public Nivel edit (Long id, CreateNivelCmd newNivel){
        Nivel nivel = findNivelById(id);

        nivel.setNombre(newNivel.nombre());

        return nivelRepository.save(nivel);
    }

    @Transactional
    public Nivel addEntrenamiento(Long idNivel, Long idEntrenamiento) {

        Nivel nivel = findNivelById(idNivel);
        Optional<Entrenamiento> entrenamientoOpt = entrenamientoRepository.findEntrenamientoById(idEntrenamiento);

        if(entrenamientoOpt.isEmpty())
            throw new EntidadNoEncontradaException("Entrenamiento no encontrado");

        Entrenamiento entrenamiento  = entrenamientoOpt.get();


        boolean yaExiste = nivel.getEntrenamientos()
                .stream()
                .anyMatch(e -> e.getId().equals(idEntrenamiento));

        if (!yaExiste) {
            nivel.addEntrenamiento(entrenamiento);
        }else{
            throw new EntidadYaAñadidaException("El entrenamiento ya está añadido a ese nivel");
        }

        return nivelRepository.save(nivel);
    }

    @Transactional
    public Nivel addEjercicio(Long idNivel, Long idEjercicio) {
        Nivel nivel = findNivelById(idNivel);
        Ejercicio ejercicio = ejercicioService.findEjercicioById(idEjercicio);

        boolean yaExiste = nivel.getEjercicios()
                .stream()
                .anyMatch(e -> e.getId().equals(idEjercicio));

        if (!yaExiste) {
            nivel.addEjercicio(ejercicio);
        }else {
            throw new EntidadYaAñadidaException("El ejercicio ya está añadido en ese nivel");
        }

        return nivelRepository.save(nivel);
    }




}
