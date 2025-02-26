package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Tipo;
import jakarta.validation.constraints.NotNull;

public record EditTipoMaterial(
        @NotNull(message = "El tipo no puede ser nulo")
        Tipo tipo
) {
}
