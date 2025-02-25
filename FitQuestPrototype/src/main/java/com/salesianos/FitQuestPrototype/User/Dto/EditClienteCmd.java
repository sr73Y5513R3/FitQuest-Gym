package com.salesianos.FitQuestPrototype.User.Dto;

import com.salesianos.FitQuestPrototype.User.Model.Genero;

public record EditClienteCmd(
        String nombre,
        String apellido1,
        String apellido2,
        String email,
        String username,
        double peso,
        double altura,
        double edad,
        Genero genero
) {
}
