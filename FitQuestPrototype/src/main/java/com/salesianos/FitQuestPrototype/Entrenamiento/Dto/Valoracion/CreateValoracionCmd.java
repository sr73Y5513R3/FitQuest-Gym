package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Valoracion;

import jakarta.validation.constraints.*;
import java.util.UUID; // Importa UUID

public record CreateValoracionCmd(
        @NotNull(message = "El ID de usuario no puede ser nulo")
        UUID idUsuario, // Añadir el ID del usuario

        @NotNull(message = "El ID del entrenamiento no puede ser nulo")
        Long idEntrenamiento, // Añadir el ID del entrenamiento (considera renombrarlo a camelCase si no lo está)

        @NotNull(message = "La nota de valoración no puede ser nula")
        @DecimalMin(value = "0.0", message = "La nota de valoración no puede ser menor que 0")
        @DecimalMax(value = "10.0", message = "La nota de valoración no puede ser mayor que 10")
        double notaValoracion
) {
}
