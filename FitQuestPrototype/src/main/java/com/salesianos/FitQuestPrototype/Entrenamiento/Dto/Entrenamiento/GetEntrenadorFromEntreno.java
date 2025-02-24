package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento;

import com.salesianos.FitQuestPrototype.User.Model.Entrenador;

import java.util.UUID;

public record GetEntrenadorFromEntreno(
        UUID id,
        String username
) {

    public static GetEntrenadorFromEntreno of(Entrenador entrenador) {
        return new GetEntrenadorFromEntreno(
                entrenador.getId(),
                entrenador.getUsername()
        );
    }
}
