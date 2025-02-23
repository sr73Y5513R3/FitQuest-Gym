package com.salesianos.FitQuestPrototype.Entrenamiento.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio.GetEjercicioDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.CreateEntrenoCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.EjercicioRepository;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.EntrenamientoRepository;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.NivelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntrenamientoService {

    private final EntrenamientoRepository entrenamientoRepository;
    private final EjercicioRepository ejercicioRepository;
    private final NivelRepository nivelRepository;


    public List<Entrenamiento> findAllEntrenamientos(){
        return entrenamientoRepository.findAllEntrenamientos();
    }


    public Entrenamiento findEntrenamientoById(Long id){
        Optional<Entrenamiento> entrenamiento = entrenamientoRepository.findById(id);

        if(entrenamiento.isEmpty())
            throw new EntityNotFoundException("Entrenamiento no encontrado");

        return entrenamiento.get();
    }

    public Entrenamiento save (CreateEntrenoCmd createEntrenoCmd){
        Entrenamiento entrenamiento = new Entrenamiento();

        entrenamiento.setNombre(createEntrenoCmd.nombre());
        entrenamiento.setDescripcion(createEntrenoCmd.descripcion());
        //entrenamiento.setDuracion(createEntrenoCmd.duracion());
        entrenamiento.setCalorias(createEntrenoCmd.calorias());
        entrenamiento.setPuntos(createEntrenoCmd.puntos());
        entrenamiento.setAutor(createEntrenoCmd.autor());

        return entrenamientoRepository.save(entrenamiento);
    }

    public Entrenamiento edit (Long id, CreateEntrenoCmd editEntreno){
        Optional<Entrenamiento> entrenamientoOpt = entrenamientoRepository.findEntrenamientoById(id);

        if (entrenamientoOpt.isEmpty())
            throw new EntityNotFoundException("Entrenamiento no encontrado");

        Entrenamiento entrenamiento = entrenamientoOpt.get();

        entrenamiento.setNombre(editEntreno.nombre());
        entrenamiento.setDescripcion(editEntreno.descripcion());
        entrenamiento.setCalorias(editEntreno.calorias());
        entrenamiento.setPuntos(editEntreno.puntos());
        entrenamiento.setAutor(editEntreno.autor());

        return entrenamientoRepository.save(entrenamiento);
    }

    @Transactional
    public Entrenamiento añadirEjercicio(Long idEntrenamiento, Long idEjercicio){
        Optional<Entrenamiento> entrenamiento = entrenamientoRepository.findEntrenamientoById(idEntrenamiento);

        Optional<Ejercicio> ejercicio = ejercicioRepository.findEjercicioById(idEjercicio);

        if (entrenamiento.isEmpty() || ejercicio.isEmpty())
            throw  new EntityNotFoundException("Entidades no encontradas con esos id");

        entrenamiento.get().addEjercicio(ejercicio.get());

        return entrenamientoRepository.save(entrenamiento.get());
    }

    @Transactional
    public Entrenamiento eliminarEjercicio(Long idEntrenamiento, Long idEjercicio){

        Optional<Entrenamiento> entrenamiento = entrenamientoRepository.findEntrenamientoById(idEntrenamiento);
        Optional<Ejercicio> ejercicio = ejercicioRepository.findEjercicioById(idEjercicio);

        if (entrenamiento.isEmpty() || ejercicio.isEmpty())
            throw  new EntityNotFoundException("Entidades no encontradas con esos id");

        entrenamiento.get().removeEjercicio(ejercicio.get());

        return entrenamientoRepository.save(entrenamiento.get());
    }

    public Entrenamiento actualizarNivel (Long id, Long idNivel){
        Optional<Entrenamiento> entrenamientoOpt = entrenamientoRepository.findEntrenamientoById(id);

        Optional<Nivel> nivelOpt = nivelRepository.findNivelById(idNivel);

        if (entrenamientoOpt.isEmpty() || nivelOpt.isEmpty())
            throw new EntityNotFoundException("Entidades no encontradas");

        Nivel nivel = nivelOpt.get();
        Entrenamiento entrenamiento = entrenamientoOpt.get();

        nivel.addEntrenamiento(entrenamiento);

        return entrenamientoRepository.save(entrenamiento);


    }
}
