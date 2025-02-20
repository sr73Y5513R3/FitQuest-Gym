package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio;

public record CreateEjercicioCmd(
        String nombre,
        String descripcion,
        double series,
        double repeticiones,
        double duracion,
        String urlImagen
) {
}
