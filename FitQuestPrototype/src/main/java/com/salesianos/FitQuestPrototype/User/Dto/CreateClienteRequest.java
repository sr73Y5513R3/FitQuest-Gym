package com.salesianos.FitQuestPrototype.User.Dto;

import com.salesianos.FitQuestPrototype.User.Model.Genero;

public record CreateClienteRequest(
        String nombre,
        String apellido1,
        String apellido2,
        String email,
        String username,
        String password,
        String verifyPassword,
        double peso,
        double altura,
        double edad,
        Genero genero
) {
}
