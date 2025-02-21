package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Nivel;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;

public record GetNivelDto (

        Long id,
        String nombre
){

    public static GetNivelDto of(Nivel nivel) {
        return new GetNivelDto(
                nivel.getId(),
                nivel.getNombre());
    }
}
