package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Tipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateMateriaCmd (
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
        String nombre,

        @NotBlank(message = "La descripción no puede estar vacía")
        @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
        String descripcion,

        @NotNull(message = "El tipo no puede ser nulo")
        Tipo tipo
){
}
