package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Realiza;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record CreateRealizaCmd(
        @NotBlank(message = "La imagen no puede estar vacío")
        //@URL(message = "La imagen tiene que ser una url")
        String imagen
) {
}
