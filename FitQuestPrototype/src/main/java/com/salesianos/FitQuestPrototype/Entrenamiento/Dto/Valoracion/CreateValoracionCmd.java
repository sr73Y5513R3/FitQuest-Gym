package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Valoracion;

public record CreateValoracionCmd(
        double notaValoracion,
        String textoDescriptivo
) {
}
