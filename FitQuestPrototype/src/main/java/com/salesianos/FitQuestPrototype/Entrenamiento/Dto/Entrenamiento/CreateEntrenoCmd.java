package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento;

import java.util.UUID;

public record CreateEntrenoCmd(
        String nombre,
        String descripcion,
        double calorias,
        double puntos,
        UUID entrenadorId
) {
}
