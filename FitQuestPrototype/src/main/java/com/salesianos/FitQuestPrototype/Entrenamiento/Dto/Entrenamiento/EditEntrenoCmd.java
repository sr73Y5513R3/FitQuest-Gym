package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record EditEntrenoCmd(
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
        String nombre,

        @NotBlank(message = "La descripción no puede estar vacía")
        @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
        String descripcion,

        @PositiveOrZero(message = "Las calorías deben ser un número positivo o cero")
        double calorias,

        @PositiveOrZero(message = "Los puntos deben ser un número positivo o cero")
        double puntos,

        @NotNull(message = "El ID del entrenador no puede ser nulo")
        UUID entrenadorId
) {
}
