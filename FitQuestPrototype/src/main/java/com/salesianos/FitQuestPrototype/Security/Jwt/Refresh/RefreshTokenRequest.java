package com.salesianos.FitQuestPrototype.Security.Jwt.Refresh;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(
        @NotNull(message = "El token de refresco no puede ser nulo")
        String refreshToken
) {
}
