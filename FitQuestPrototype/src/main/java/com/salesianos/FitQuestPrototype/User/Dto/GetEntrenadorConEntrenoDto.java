package com.salesianos.FitQuestPrototype.User.Dto;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.GetEntrenoFromEntrenador;
import com.salesianos.FitQuestPrototype.User.Model.Entrenador;
import com.salesianos.FitQuestPrototype.User.Model.UserRole;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record GetEntrenadorConEntrenoDto (
        UUID id,
        String Username,
        Set<UserRole> roles,
        List<GetEntrenoFromEntrenador> entrenos
){

    public static GetEntrenadorConEntrenoDto of (Entrenador entrenador){
        return new GetEntrenadorConEntrenoDto(
                entrenador.getId(),
                entrenador.getUsername(),
                entrenador.getRoles(),
                entrenador.getEntrenamientos().stream()
                        .map(GetEntrenoFromEntrenador::of).toList()
        );
    }
}
