package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;

public record GetEjercicioFromEntrenoDto(
        Long id,
        String nombre
) {
    public static GetEjercicioFromEntrenoDto of(Ejercicio ejercicio) {
        return new GetEjercicioFromEntrenoDto(
                ejercicio.getId(),
                ejercicio.getNombre()
        );
    }
}
