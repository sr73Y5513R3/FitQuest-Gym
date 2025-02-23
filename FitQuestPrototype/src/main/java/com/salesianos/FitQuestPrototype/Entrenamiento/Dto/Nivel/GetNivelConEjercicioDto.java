package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Nivel;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio.GetEjercicioFromNivelDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.GetEntrenamientoFromNivel;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;

import java.util.List;

public record GetNivelConEjercicioDto(
        Long id,
        String nombre,
        List<GetEjercicioFromNivelDto> ejercicios,
        List<GetEntrenamientoFromNivel> entrenamientos
) {

    public static GetNivelConEjercicioDto of(Nivel nivel) {
        return new GetNivelConEjercicioDto(
                nivel.getId(),
                nivel.getNombre(),
                nivel.getEjercicios().stream()
                        .map(GetEjercicioFromNivelDto::of)
                        .toList(),
                nivel.getEntrenamientos()
                        .stream()
                        .map(GetEntrenamientoFromNivel::of)
                        .toList()
        );
    }
}
