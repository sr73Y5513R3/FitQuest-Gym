package com.salesianos.FitQuestPrototype.Entrenamiento.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.CreateEntrenoCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.EditEntrenoCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.EntrenamientoRepository;
import com.salesianos.FitQuestPrototype.User.Model.Entrenador;
import com.salesianos.FitQuestPrototype.User.Services.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntrenamientoService {

    private final EntrenamientoRepository entrenamientoRepository;
    private final EjercicioService ejercicioService;
    private final NivelService nivelService;
    private final UsuarioService entrenadorService;


    public Page<Entrenamiento> findAllEntrenamientos(Pageable pageable) {
        return entrenamientoRepository.findAllEntrenamientos(pageable);
    }


    public Entrenamiento findEntrenamientoById(Long id){
        Optional<Entrenamiento> entrenamiento = entrenamientoRepository.findById(id);

        if(entrenamiento.isEmpty())
            throw new EntityNotFoundException("Entrenamiento no encontrado");

        return entrenamiento.get();
    }

    @Transactional
    public Entrenamiento save (CreateEntrenoCmd createEntrenoCmd){
        Entrenamiento entrenamiento = new Entrenamiento();

        entrenamiento.setNombre(createEntrenoCmd.nombre());
        entrenamiento.setDescripcion(createEntrenoCmd.descripcion());
        entrenamiento.setCalorias(createEntrenoCmd.calorias());
        entrenamiento.setPuntos(createEntrenoCmd.puntos());

        Entrenador entrenador = entrenadorService.findEntrenadorById(createEntrenoCmd.entrenadorId());

        entrenador.addEntrenamiento(entrenamiento);

        entrenamiento.setEntrenador(entrenador);



        return entrenamientoRepository.save(entrenamiento);
    }

    @Transactional
    public Entrenamiento edit (Long id, EditEntrenoCmd editEntreno){


        Entrenamiento entrenamiento = findEntrenamientoById(id);

        entrenamiento.setNombre(editEntreno.nombre());
        entrenamiento.setDescripcion(editEntreno.descripcion());
        entrenamiento.setCalorias(editEntreno.calorias());
        entrenamiento.setPuntos(editEntreno.puntos());

        return entrenamientoRepository.save(entrenamiento);
    }

    @Transactional
    public Entrenamiento añadirEjercicio(Long idEntrenamiento, Long idEjercicio){
        Entrenamiento entrenamiento = findEntrenamientoById(idEntrenamiento);

        Ejercicio ejercicio = ejercicioService.findEjercicioById(idEjercicio);

        return entrenamientoRepository.save(entrenamiento);
    }

    @Transactional
    public Entrenamiento eliminarEjercicio(Long idEntrenamiento, Long idEjercicio){

        Entrenamiento entrenamiento = findEntrenamientoById(idEntrenamiento);
        Ejercicio ejercicio = ejercicioService.findEjercicioById(idEjercicio);

        return entrenamientoRepository.save(entrenamiento);
    }

    public Entrenamiento actualizarNivel (Long id, Long idNivel){
        Entrenamiento entrenamiento =findEntrenamientoById(id);

        Nivel nivel = nivelService.findNivelById(idNivel);

        nivel.addEntrenamiento(entrenamiento);

        return entrenamientoRepository.save(entrenamiento);

    }

    public Entrenamiento findByNombre(String nombre){
        Optional<Entrenamiento> entrenamiento = entrenamientoRepository.findEntrenamientoByNombre(nombre);

        if (entrenamiento.isEmpty())
            throw new EntityNotFoundException("Entrenamiento no encontrado");

        return entrenamiento.get();
    }

    public boolean isEntrenador(Long idEntrenamiento) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            return false; // Usuario no autenticado
        }

        Entrenamiento entrenamiento = findEntrenamientoById(idEntrenamiento);
        return entrenamiento.getEntrenador().getUsername().equals(username);
    }
}
