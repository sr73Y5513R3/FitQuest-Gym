package com.salesianos.FitQuestPrototype.Entrenamiento.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Nivel.CreateNivelCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.NivelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NivelService {

    private final NivelRepository nivelRepository;

    public List<Nivel> findAllNiveles(){
        return nivelRepository.findAllNiveles();
    }

    public Nivel save (CreateNivelCmd newNivel){
        return nivelRepository.save(Nivel.builder()
                .nombre(newNivel.nombre())
                .build());
    }
}
