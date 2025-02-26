package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;

public record GetEjercicioDto(
        Long id,
        String nombre,
        String descripcion,
        int series,
        int repeticiones,
        double duracion,
        String urlImagen
) {

    public static GetEjercicioDto of(Ejercicio ejercicio) {
        return new GetEjercicioDto(
                ejercicio.getId(),
                ejercicio.getNombre(),
                ejercicio.getDescripcion(),
                ejercicio.getSeries(),
                ejercicio.getRepeticiones(),
                ejercicio.getDuracion(),
                ejercicio.getUrlImagenes()
        );
    }
}
