package com.salesianos.FitQuestPrototype.Entrenamiento.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.CreateEntrenoCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.EntrenamientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntrenamientoService {

    private final EntrenamientoRepository entrenamientoRepository;

    public Entrenamiento save (CreateEntrenoCmd createEntrenoCmd){
        Entrenamiento entrenamiento = new Entrenamiento();

        entrenamiento.setNombre(createEntrenoCmd.nombre());
        entrenamiento.setDescripcion(createEntrenoCmd.descripcion());
        entrenamiento.setDuracion(createEntrenoCmd.duracion());
        entrenamiento.setCalorias(createEntrenoCmd.calorias());
        entrenamiento.setPuntos(createEntrenoCmd.puntos());
        entrenamiento.setAutor(createEntrenoCmd.autor());

        return entrenamientoRepository.save(entrenamiento);
    }
}
