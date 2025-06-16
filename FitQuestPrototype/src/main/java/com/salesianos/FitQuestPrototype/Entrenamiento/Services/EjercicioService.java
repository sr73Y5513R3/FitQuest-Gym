package com.salesianos.FitQuestPrototype.Entrenamiento.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio.CreateEjercicioCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Error.BorradoIlegalException;
import com.salesianos.FitQuestPrototype.Entrenamiento.Error.EntidadNoEncontradaException;
import com.salesianos.FitQuestPrototype.Entrenamiento.Error.EntidadYaAñadidaException;
import com.salesianos.FitQuestPrototype.Entrenamiento.Error.NoContainsException;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Material;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.EjercicioRepository;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.NivelRepository;
import com.salesianos.FitQuestPrototype.User.Error.EqualLevelException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EjercicioService {

    private final EjercicioRepository ejercicioRepository;
    private final NivelRepository nivelRepository;
    private final MaterialService materialService;

    public Page<Ejercicio> findAllEjercicio(Pageable pageable){
        return ejercicioRepository.findAllEjercicios(pageable);
    }

    public Ejercicio findEjercicioById(Long id){
        Optional<Ejercicio> ejercicio = ejercicioRepository.findById(id);

        if (ejercicio.isEmpty())
            throw new EntidadNoEncontradaException("Ejercicio no encontrado con ese id");

        return ejercicio.get();
    }

    public Ejercicio save (CreateEjercicioCmd createEjercicioCmd){

        Nivel nivel = nivelRepository.findById(createEjercicioCmd.nivel().getId())
                .orElseThrow(() -> new EntityNotFoundException("Nivel no encontrado con ID: " + createEjercicioCmd.nivel().getId()));

        return ejercicioRepository.save(Ejercicio.builder()
                .nombre(createEjercicioCmd.nombre())
                .descripcion(createEjercicioCmd.descripcion())
                .series(createEjercicioCmd.series())
                .repeticiones(createEjercicioCmd.repeticiones())
                .duracion(createEjercicioCmd.duracion())
                .urlImagenes(createEjercicioCmd.urlImagen())
                .nivel(nivel)
                .build());
    }

    public Ejercicio edit (Long id, CreateEjercicioCmd createEjercicioCmd){
        Ejercicio ejercicio = findEjercicioById(id);
        Nivel nivel = nivelRepository.findById(createEjercicioCmd.nivel().getId())
                .orElseThrow(() -> new EntityNotFoundException("Nivel no encontrado con ID: " + createEjercicioCmd.nivel().getId()));

        ejercicio.setNombre(createEjercicioCmd.nombre());
        ejercicio.setDescripcion(createEjercicioCmd.descripcion());
        ejercicio.setSeries(createEjercicioCmd.series());
        ejercicio.setRepeticiones(createEjercicioCmd.repeticiones());
        ejercicio.setDuracion(createEjercicioCmd.duracion());
        ejercicio.setUrlImagenes(createEjercicioCmd.urlImagen());
        ejercicio.setNivel(nivel);

        return ejercicioRepository.save(ejercicio);
    }

    public Ejercicio actualizarNivel (Long id, Long idNivel){
        Ejercicio ejercicio = findEjercicioById(id);

        Optional<Nivel> nivel = nivelRepository.findNivelById(idNivel);

        nivel.get().addEjercicio(ejercicio);

        if(ejercicio.getNivel().getId() == nivel.get().getId())
            throw new EqualLevelException("No puedes cambiar el nivel si es el mismo al que ya tiene");

        return ejercicioRepository.save(ejercicio);


    }

    public Ejercicio findByNombre(String nombre){
        Optional<Ejercicio> ejercicioOpt = ejercicioRepository.findEjercicioByNombre(nombre);

        if (ejercicioOpt.isEmpty())
            throw new EntidadNoEncontradaException("Ejercicio no encontrado con ese nombre");

        return ejercicioOpt.get();
    }

    @Transactional
    public Ejercicio addMaterial (Long idEjercicio, Long idMaterial){
        Ejercicio ejercicio = findEjercicioById(idEjercicio);

        Material material = materialService.findMaterialById(idMaterial);

        if (ejercicio.getMateriales().contains(material))
            throw new EntidadYaAñadidaException("El material ya está añadido a este ejercicio");

        ejercicio.addMaterial(material);

        return ejercicioRepository.save(ejercicio);

    }

    @Transactional
    public Ejercicio removeMaterial (Long idEjercicio, Long idMaterial){
        Ejercicio ejercicio = findEjercicioById(idEjercicio);

        Material material = materialService.findMaterialById(idMaterial);

        if(!ejercicio.getMateriales().contains(material))
            throw new NoContainsException("Si no está ese material no lo puedo borrar picha");

        ejercicio.getMateriales().remove(material);

        return ejercicioRepository.save(ejercicio);
    }

    public void removeEjercicio (Long idEjercicio){
        Ejercicio ejercicio = findEjercicioById(idEjercicio);

        if (!ejercicio.getEntrenamientos().isEmpty())
            throw new BorradoIlegalException("No se puede eliminar el entrenamiento");

        ejercicioRepository.delete(ejercicio);
    }

}
