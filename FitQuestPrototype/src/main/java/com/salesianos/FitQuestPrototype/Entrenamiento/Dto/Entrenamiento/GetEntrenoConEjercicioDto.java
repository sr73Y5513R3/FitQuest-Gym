package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio.GetEjercicioFromEntrenoDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Nivel.GetNivelDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Valoracion;

import java.util.Set;
import java.util.stream.Collectors;

public record GetEntrenoConEjercicioDto(
        Long id,
        String nombre,
        String descripcion,
        double duracion,
        double calorias,
        double puntos,
        double valoracionMedia,
        GetEntrenadorFromEntreno entrenador,
        Set<GetEjercicioFromEntrenoDto> ejercicios,
        GetNivelDto nivel
) {

    public static GetEntrenoConEjercicioDto of(Entrenamiento entrenamiento){

        double duracionTotal = entrenamiento.getEjercicios().stream()
                .mapToDouble(Ejercicio::getDuracion)
                .sum();

        double valorarcion = entrenamiento.getValoraciones().stream()
                .mapToDouble(Valoracion::getNotaValoracion)
                .average().orElse(0.0);

        return new GetEntrenoConEjercicioDto(
                entrenamiento.getId(),
                entrenamiento.getNombre(),
                entrenamiento.getDescripcion(),
                duracionTotal,
                entrenamiento.getCalorias(),
                entrenamiento.getPuntos(),
                valorarcion,
                GetEntrenadorFromEntreno.of(entrenamiento.getEntrenador()),
                entrenamiento.getEjercicios()
                        .stream()
                        .map(GetEjercicioFromEntrenoDto::of)
                        .collect(Collectors.toSet()),
                GetNivelDto.of(entrenamiento.getNivel())
        );
    }
}
