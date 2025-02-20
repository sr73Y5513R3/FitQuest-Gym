package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;

public record GetEntrenamientoDto(

        Long id,
        String nombre,
        String descripcion,
        double duracion,
        double calorias,
        double puntos,
        String autor
) {

    public static GetEntrenamientoDto of(Entrenamiento entrenamiento){
        return new GetEntrenamientoDto(
                entrenamiento.getId(),
                entrenamiento.getNombre(),
                entrenamiento.getDescripcion(),
                entrenamiento.getDuracion(),
                entrenamiento.getCalorias(),
                entrenamiento.getPuntos(),
                entrenamiento.getAutor()
        );
    }
}
