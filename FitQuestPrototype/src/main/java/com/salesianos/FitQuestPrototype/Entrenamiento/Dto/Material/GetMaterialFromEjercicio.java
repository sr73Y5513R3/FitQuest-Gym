package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Material;

public record GetMaterialFromEjercicio(
        Long id,
        String nombre
) {

    public static GetMaterialFromEjercicio of (Material material) {
        return new GetMaterialFromEjercicio(
                material.getId(),
                material.getNombre()
        );
    }
}
