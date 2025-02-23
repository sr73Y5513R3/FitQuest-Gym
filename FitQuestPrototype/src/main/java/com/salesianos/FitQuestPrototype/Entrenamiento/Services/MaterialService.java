package com.salesianos.FitQuestPrototype.Entrenamiento.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material.CreateMateriaCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material.EditMaterialCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material.EditTipoMaterial;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Material;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.MaterialRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Material findMaterialById(Long id) {
        Optional<Material> material = materialRepository.findMaterialById(id);

        if (material.isEmpty())
            throw new EntityNotFoundException("Material no encontrado con ese id");

        return material.get();

    }

    public Material editTipoMaterial (Long id, EditTipoMaterial editTipo){
        Optional<Material> material = materialRepository.findMaterialById(id);

        if(material.isEmpty())
            throw new EntityNotFoundException("Material no encontrado con ese id");

        material.get().setTipo(editTipo.tipo());

        return materialRepository.save(material.get());
    }

    public Material editMaterial (Long id, EditMaterialCmd editMaterial){
        Optional<Material> materialOpt = materialRepository.findMaterialById(id);

        if (materialOpt.isEmpty())
            throw new EntityNotFoundException("Material no encontrado con ese id");

        Material material = materialOpt.get();

        material.setNombre(editMaterial.nombre());
        material.setDescripcion(editMaterial.descripcion());

        return materialRepository.save(material);
    }

}
