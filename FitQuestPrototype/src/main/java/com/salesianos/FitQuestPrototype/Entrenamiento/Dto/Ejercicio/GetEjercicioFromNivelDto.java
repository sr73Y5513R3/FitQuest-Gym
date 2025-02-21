package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;

public record GetEjercicioFromNivelDto(
        Long id,
        String nombre
) {

    public static GetEjercicioFromNivelDto of(Ejercicio ejercicio) {
        return new GetEjercicioFromNivelDto(
                ejercicio.getId(),
                ejercicio.getNombre()
        );
    }
}
