package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Realiza;

import com.salesianos.FitQuestPrototype.User.Model.Usuario;

import java.util.UUID;

public record GetUsuarioFromRealiza(
        UUID id,
        String username
) {
    public static GetUsuarioFromRealiza of(Usuario usuario) {
        return new GetUsuarioFromRealiza(
                usuario.getId(),
                usuario.getUsername()
        );
    }
}
