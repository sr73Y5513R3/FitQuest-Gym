package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;

public record GetEntrenamientoFromNivel (
        Long id,
        String nombre
){

    public static GetEntrenamientoFromNivel of (Entrenamiento entrenamiento) {
        return new GetEntrenamientoFromNivel (
                entrenamiento.getId(),
                entrenamiento.getNombre()
        );
    }
}
