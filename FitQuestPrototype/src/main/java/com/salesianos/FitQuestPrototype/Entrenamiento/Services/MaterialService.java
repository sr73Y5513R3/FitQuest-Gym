package com.salesianos.FitQuestPrototype.Entrenamiento.Services;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material.CreateMateriaCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material.EditMaterialCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material.EditTipoMaterial;
import com.salesianos.FitQuestPrototype.Entrenamiento.Error.EntidadNoEncontradaException;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Material;
import com.salesianos.FitQuestPrototype.Entrenamiento.Repos.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<Material> findAllMateriales(Pageable pageable) {
        return materialRepository.FindAllMateriales(pageable);
    }

    public Material findMaterialById(Long id) {
        Optional<Material> material = materialRepository.findMaterialById(id);

        if (material.isEmpty())
            throw new EntidadNoEncontradaException("No existen materiales con ese id");

        return material.get();

    }

    public Material editTipoMaterial (Long id, EditTipoMaterial editTipo){
        Material material = findMaterialById(id);

        material.setTipo(editTipo.tipo());

        return materialRepository.save(material);
    }

    public Material editMaterial (Long id, EditMaterialCmd editMaterial){
        Material material = findMaterialById(id);

        material.setNombre(editMaterial.nombre());
        material.setDescripcion(editMaterial.descripcion());

        return materialRepository.save(material);
    }

    public void borrarMaterial(Long id) {
        Material material = findMaterialById(id);

        materialRepository.delete(material);
    }

}
