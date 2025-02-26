package com.salesianos.FitQuestPrototype.User.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
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

        @NotBlank(message = "La contraseña no puede estar vacía")
        @Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres")
        String password,

        @NotBlank(message = "La verificación de la contraseña no puede estar vacía")
        @Size(min = 6, max = 20, message = "La verificación de la contraseña debe tener entre 6 y 20 caracteres")
        String verifyPassword
){
}
