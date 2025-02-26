package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Nivel.GetNivelDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;

public record GetEjercicioConNivelDto(
        Long id,
        String nombre,
        String descripcion,
        int series,
        int repeticiones,
        double duracion,
        String urlImagen,
        GetNivelDto nivel
) {

    public static GetEjercicioConNivelDto of(Ejercicio ejercicio) {
        return new GetEjercicioConNivelDto(
                ejercicio.getId(),
                ejercicio.getNombre(),
                ejercicio.getDescripcion(),
                ejercicio.getSeries(),
                ejercicio.getRepeticiones(),
                ejercicio.getDuracion(),
                ejercicio.getUrlImagenes(),
                GetNivelDto.of(ejercicio.getNivel())
        );
    }
}
