package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Valoracion;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Realiza.GetUsuarioFromRealiza;
import com.salesianos.FitQuestPrototype.User.Model.Usuario;

import java.util.UUID;

public record GetUsuarioFromValorar(
        UUID id,
        String username
) {

    public static GetUsuarioFromValorar of(Usuario usuario) {
        return new GetUsuarioFromValorar(
                usuario.getId(),
                usuario.getUsername()
        );
    }
}
