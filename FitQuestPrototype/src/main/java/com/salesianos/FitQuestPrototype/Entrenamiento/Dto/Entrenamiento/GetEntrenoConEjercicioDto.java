package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio.GetEjercicioFromEntrenoDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;

import java.util.Set;
import java.util.stream.Collectors;

public record GetEntrenoConEjercicioDto(
        Long id,
        String nombre,
        String descripcion,
        double duracion,
        double calorias,
        double puntos,
        String autor,
        Set<GetEjercicioFromEntrenoDto> ejercicios
) {

    public static GetEntrenoConEjercicioDto of(Entrenamiento entrenamiento){

        double duracionTotal = entrenamiento.getEjercicios().stream()
                .mapToDouble(Ejercicio::getDuracion)
                .sum();

        return new GetEntrenoConEjercicioDto(
                entrenamiento.getId(),
                entrenamiento.getNombre(),
                entrenamiento.getDescripcion(),
                duracionTotal,
                entrenamiento.getCalorias(),
                entrenamiento.getPuntos(),
                entrenamiento.getAutor(),
                entrenamiento.getEjercicios()
                        .stream()
                        .map(GetEjercicioFromEntrenoDto::of)
                        .collect(Collectors.toSet())
        );
    }
}
