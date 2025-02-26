package com.salesianos.FitQuestPrototype.User.Dto;

import com.salesianos.FitQuestPrototype.User.Model.Genero;
import jakarta.validation.constraints.*;

public record EditClienteCmd(
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        String nombre,

        @NotBlank(message = "El primer apellido no puede estar vacío")
        @Size(min = 2, max = 50, message = "El primer apellido debe tener entre 2 y 50 caracteres")
        String apellido1,

        @Size(min = 2, max = 50, message = "El segundo apellido debe tener entre 2 y 50 caracteres")
        String apellido2,

        @NotBlank(message = "El email no puede estar vacío")
        @Email(message = "El email debe ser válido")
        String email,

        @NotBlank(message = "El nombre de usuario no puede estar vacío")
        @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres")
        String username,

        @NotNull(message = "El peso no puede ser nulo")
        @Positive(message = "El peso debe ser un número positivo")
        double peso,

        @NotNull(message = "La altura no puede ser nula")
        @Positive(message = "La altura debe ser un número positivo")
        double altura,

        @NotNull(message = "La edad no puede ser nula")
        @Positive(message = "La edad debe ser un número positivo")
        double edad,

        @NotNull(message = "El género no puede ser nulo")
        Genero genero
) {
}
