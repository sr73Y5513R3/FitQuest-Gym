package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material.GetMaterialFromEjercicio;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Nivel.GetNivelDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Ejercicio;

import java.util.Set;
import java.util.stream.Collectors;

public record GetEjercicioConNivelDto(
        Long id,
        String nombre,
        String descripcion,
        int series,
        int repeticiones,
        double duracion,
        String urlImagen,
        GetNivelDto nivel,
        Set<GetMaterialFromEjercicio> materiales
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
                GetNivelDto.of(ejercicio.getNivel()),
                ejercicio.getMateriales()
                        .stream()
                        .map(GetMaterialFromEjercicio::of)
                        .collect(Collectors.toSet())
        );
    }
}
