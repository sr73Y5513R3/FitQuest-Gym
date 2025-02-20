package com.salesianos.FitQuestPrototype.Entrenamiento.Dto;

public record CreateEntrenoCmd(
        String nombre,
        String descripcion,
        double duracion,
        double calorias,
        double puntos,
        String autor
) {
}
