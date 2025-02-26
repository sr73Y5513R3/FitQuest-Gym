package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Valoracion;

import jakarta.validation.constraints.*;

public record CreateValoracionCmd(
        @NotNull(message = "La nota de valoración no puede ser nula")
        @DecimalMin(value = "0.0", message = "La nota de valoración no puede ser menor que 0")
        @DecimalMax(value = "10.0", message = "La nota de valoración no puede ser mayor que 10")
        double notaValoracion,

        @NotBlank(message = "El texto descriptivo no puede estar vacío")
        @Size(max = 500, message = "El texto descriptivo no puede exceder los 500 caracteres")
        String textoDescriptivo
) {
}
