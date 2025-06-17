package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Realiza;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AceptarRealizaDto(
        @NotNull(message = "El ID de usuario no puede ser nulo")
        UUID idUsuario,

        @NotNull(message = "El ID de entrenamiento no puede ser nulo")
        Long idEntrenamiento,

        @NotNull(message = "El ID de entrenador no puede ser nulo")
        UUID idEntrenador)
{}
