package com.salesianos.FitQuestPrototype.User.Dto;

import com.salesianos.FitQuestPrototype.User.Model.UserRole;
import com.salesianos.FitQuestPrototype.User.Model.Usuario;

import java.util.Set;
import java.util.UUID;

public record GetUsuarioDto(
        UUID id,
        String username,
        String email,
        Set<UserRole> roles
) {

    public static GetUsuarioDto of(Usuario usuario) {
        return new GetUsuarioDto(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getRoles()
        );
    }
}
