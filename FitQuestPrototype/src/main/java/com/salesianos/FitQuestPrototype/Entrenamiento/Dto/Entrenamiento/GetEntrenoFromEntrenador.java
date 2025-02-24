package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;

public record GetEntrenoFromEntrenador(
        Long id,
        String nombre,
        String descripcion
) {
    public static GetEntrenoFromEntrenador of(Entrenamiento entrenamiento) {
        return new GetEntrenoFromEntrenador(
                entrenamiento.getId(), entrenamiento.getNombre(), entrenamiento.getDescripcion()
        );
    }
}
