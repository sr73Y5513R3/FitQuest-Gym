package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Nivel.GetNivelDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;

public record GetEntrenoConNivelDto(
        Long id,
        String nombre,
        String descripcion,
        double duracion,
        double calorias,
        double puntos,
        String autor,
        GetNivelDto nivel
) {
    public static GetEntrenoConNivelDto of(Entrenamiento entrenamiento) {
        return new GetEntrenoConNivelDto(
                entrenamiento.getId(),
                entrenamiento.getNombre(),
                entrenamiento.getDescripcion(),
                entrenamiento.getDuracion(),
                entrenamiento.getCalorias(),
                entrenamiento.getPuntos(),
                entrenamiento.getAutor(),
                GetNivelDto.of(entrenamiento.getNivel())
        );
    }
}
