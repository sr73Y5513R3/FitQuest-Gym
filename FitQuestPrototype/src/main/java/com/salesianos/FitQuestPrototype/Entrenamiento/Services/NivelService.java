package com.salesianos.FitQuestPrototype.Entrenamiento.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Nivel.CreateNivelCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.NivelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NivelService {

    private final NivelRepository nivelRepository;

    public List<Nivel> findAllNiveles(){
        return nivelRepository.findAllNiveles();
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
}
