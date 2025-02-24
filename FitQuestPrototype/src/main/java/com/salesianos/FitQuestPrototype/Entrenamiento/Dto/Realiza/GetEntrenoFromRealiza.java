package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Realiza;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;

public record GetEntrenoFromRealiza(
        Long id,
        String nombre
) {
    public static GetEntrenoFromRealiza of(Entrenamiento entrenamiento) {
        return new GetEntrenoFromRealiza(
                entrenamiento.getId(),
                entrenamiento.getNombre()
        );
    }
}
