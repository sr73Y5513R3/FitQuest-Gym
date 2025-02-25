package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Valoracion;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;

public record GetEntrenoFromValorar(
        Long id,
        String nombre
) {

    public static GetEntrenoFromValorar of(Entrenamiento entrenamiento) {
        return new GetEntrenoFromValorar(
                entrenamiento.getId(),
                entrenamiento.getNombre()
        );
    }
}
