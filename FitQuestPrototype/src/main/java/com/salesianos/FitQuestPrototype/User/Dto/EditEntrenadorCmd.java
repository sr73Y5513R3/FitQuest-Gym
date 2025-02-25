package com.salesianos.FitQuestPrototype.User.Dto;

public record EditEntrenadorCmd(
        String nombre,
        String apellido1,
        String apellido2,
        String email,
        String username
) {
}
