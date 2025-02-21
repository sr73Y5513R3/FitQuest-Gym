package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Material;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Tipo;

public record GetMaterialDto (
        Long id,
        String nombre,
        String descripcion,
        Tipo tipo
){

    public static GetMaterialDto of(Material material){
        return new GetMaterialDto(
                material.getId(),
                material.getNombre(),
                material.getDescripcion(),
                material.getTipo()
        );
    }
}
