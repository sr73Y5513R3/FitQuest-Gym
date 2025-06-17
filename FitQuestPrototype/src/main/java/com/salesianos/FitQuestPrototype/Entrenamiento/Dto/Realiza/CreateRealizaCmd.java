package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Realiza;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public record CreateRealizaCmd(

        @NotNull
        UUID idUsuario,

        @NotNull
        Long idEntrenamiento


) {
}
