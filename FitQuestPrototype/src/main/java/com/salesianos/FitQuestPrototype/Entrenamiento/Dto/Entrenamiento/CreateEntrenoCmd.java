package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento;

public record CreateEntrenoCmd(
        String nombre,
        String descripcion,
        double calorias,
        double puntos,
        String autor
) {
}
