package com.salesianos.FitQuestPrototype.User.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesianos.FitQuestPrototype.User.Model.UserRole;
import com.salesianos.FitQuestPrototype.User.Model.Usuario;

import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String token,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String refreshToken,
        Set<UserRole> roles
) {
    public static UserResponse of (Usuario user) {
        return new UserResponse(user.getId(), user.getUsername(), null, null,user.getRoles());
    }

    public static UserResponse of (Usuario user, String token, String refreshToken) {
        return new UserResponse(user.getId(), user.getUsername(), token, refreshToken,user.getRoles());
    }

}
