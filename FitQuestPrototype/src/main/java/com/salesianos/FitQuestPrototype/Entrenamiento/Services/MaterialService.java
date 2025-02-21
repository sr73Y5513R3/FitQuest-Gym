package com.salesianos.FitQuestPrototype.Entrenamiento.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material.CreateMateriaCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Material;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;

    public Material save(CreateMateriaCmd newMaterial) {
        return materialRepository.save(Material.builder()
                .nombre(newMaterial.nombre())
                .descripcion(newMaterial.descripcion())
                .tipo(newMaterial.tipo())
                .build());
    }

    public List<Material> findAllMateriales() {
        return materialRepository.FindAllMateriales();
    }

}
