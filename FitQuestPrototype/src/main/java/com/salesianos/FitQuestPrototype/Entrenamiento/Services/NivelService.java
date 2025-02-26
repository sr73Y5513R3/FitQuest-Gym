package com.salesianos.FitQuestPrototype.Entrenamiento.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Nivel.CreateNivelCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.NivelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NivelService {

    private final NivelRepository nivelRepository;
    private final EntrenamientoService entrenamientoService;
    private final EjercicioService ejercicioService;

    public Page<Nivel> findAllNiveles(Pageable pageable) {
        return nivelRepository.findAllNiveles(pageable);
    }

    public Nivel findNivelById(Long id){
        Optional<Nivel> nivel = nivelRepository.findById(id);

        if(nivel.isEmpty())
            throw new EntityNotFoundException("Nivel no encontrado con ese id");

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
        Entrenamiento entrenamiento = entrenamientoService.findEntrenamientoById(idEntrenamiento);


        boolean yaExiste = nivel.getEntrenamientos()
                .stream()
                .anyMatch(e -> e.getId().equals(idEntrenamiento));

        if (!yaExiste) {
            nivel.addEntrenamiento(entrenamiento);
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
        }

        return nivelRepository.save(nivel);
    }




}
