package com.salesianos.FitQuestPrototype.User.Dto;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Nivel.GetNivelDto;
import com.salesianos.FitQuestPrototype.User.Model.Cliente;
import com.salesianos.FitQuestPrototype.User.Model.Genero;
import com.salesianos.FitQuestPrototype.User.Model.Mensualidad;
import com.salesianos.FitQuestPrototype.User.Model.UserRole;

import java.util.Set;
import java.util.UUID;

public record GetClienteDto(
        UUID id,
        String username,
        String email,
        Set<UserRole> roles,
        double peso,
        double altura,
        double edad,
        Genero genero,
        Mensualidad mensualidad,
        GetNivelDto nivel
) {

    public static GetClienteDto of(Cliente cliente) {
        return new GetClienteDto(
                cliente.getId(),
                cliente.getUsername(),
                cliente.getEmail(),
                cliente.getRoles(),
                cliente.getPeso(),
                cliente.getAltura(),
                cliente.getEdad(),
                cliente.getGenero(),
                cliente.getMensualidad(),
                GetNivelDto.of(cliente.getNivel())
        );
    }
}
