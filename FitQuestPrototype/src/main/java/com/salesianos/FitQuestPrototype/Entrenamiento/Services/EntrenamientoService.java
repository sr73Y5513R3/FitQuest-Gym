package com.salesianos.FitQuestPrototype.Entrenamiento.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.CreateEntrenoCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.EditEntrenoCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Error.EntidadNoEncontradaException;
import com.salesianos.FitQuestPrototype.Entrenamiento.Error.EntidadYaAñadidaException;
import com.salesianos.FitQuestPrototype.Entrenamiento.Error.NoContainsException;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.EntrenamientoRepository;
import com.salesianos.FitQuestPrototype.User.Error.EqualLevelException;
import com.salesianos.FitQuestPrototype.User.Model.Entrenador;
import com.salesianos.FitQuestPrototype.User.Services.UsuarioService;
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
            throw new EntidadNoEncontradaException("Entrenamiento no encontrado");

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

        if(entrenamiento.getEjercicios().contains(ejercicio)){
            throw new EntidadYaAñadidaException("El ejercicio ya está añadido");
        }

        entrenamiento.addEjercicio(ejercicio);

        return entrenamientoRepository.save(entrenamiento);
    }

    @Transactional
    public Entrenamiento eliminarEjercicio(Long idEntrenamiento, Long idEjercicio){

        Entrenamiento entrenamiento = findEntrenamientoById(idEntrenamiento);
        Ejercicio ejercicio = ejercicioService.findEjercicioById(idEjercicio);

        if (!entrenamiento.getEjercicios().contains(ejercicio)){
            throw new NoContainsException("El ejercicio no está añadido a ese entrenamiento, como quieres que lo borre?");
        }

        entrenamiento.removeEjercicio(ejercicio);

        return entrenamientoRepository.save(entrenamiento);
    }

    public Entrenamiento actualizarNivel (Long id, Long idNivel){
        Entrenamiento entrenamiento =findEntrenamientoById(id);

        Nivel nivel = nivelService.findNivelById(idNivel);

        if(entrenamiento.getNivel().getId() == nivel.getId())
            throw new EqualLevelException("No puedes cambiar el nivel si es el mismo nivel que ya tiene");

        nivel.addEntrenamiento(entrenamiento);

        return entrenamientoRepository.save(entrenamiento);

    }

    public Entrenamiento findByNombre(String nombre){
        Optional<Entrenamiento> entrenamiento = entrenamientoRepository.findEntrenamientoByNombre(nombre);

        if (entrenamiento.isEmpty())
            throw new EntidadNoEncontradaException("Entrenamiento no encontrado con ese nombre");

        return entrenamiento.get();
    }

    public boolean isEntrenador(Long idEntrenamiento) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            return false;
        }

        Entrenamiento entrenamiento = findEntrenamientoById(idEntrenamiento);
        return entrenamiento.getEntrenador().getUsername().equals(username);
    }
}
