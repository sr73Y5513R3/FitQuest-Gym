package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

public record CreateEjercicioCmd(
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
        String nombre,

        @NotBlank(message = "La descripción no puede estar vacía")
        @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
        String descripcion,

        @PositiveOrZero(message = "Las calorías deben ser un número positivo o cero")
        @Min(1)
        int series,

        @PositiveOrZero(message = "Las repeticiones deben ser un número positivo o cero")
        @Min(1)
        int repeticiones,

        @Positive(message = "La duración deben ser un número positivo")
        double duracion,

        @NotBlank(message = "La URL de la imagen no puede estar vacía")
        //@URL(message = "La URL de la imagen debe ser válida")
        String urlImagen
) {
}
