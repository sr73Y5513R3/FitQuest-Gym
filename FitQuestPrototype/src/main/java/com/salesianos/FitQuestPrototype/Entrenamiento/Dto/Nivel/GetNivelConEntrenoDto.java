package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Nivel;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.GetEntrenamientoDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;

import java.util.List;

public record GetNivelConEntrenoDto(
        Long id,
        String nombre,
        List<GetEntrenamientoDto> entrenamientos
) {

    public static GetNivelConEntrenoDto of(Nivel nivel) {
        return new GetNivelConEntrenoDto(
                nivel.getId(),
                nivel.getNombre(),
                nivel.getEntrenamientos()
                        .stream()
                        .map(GetEntrenamientoDto::of)
                        .toList()
        );
    }
}
