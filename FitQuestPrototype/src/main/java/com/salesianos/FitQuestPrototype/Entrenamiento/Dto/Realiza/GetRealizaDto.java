package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Realiza;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Realiza;

public record GetRealizaDto(
        Boolean realizado,
        String imagen,
        GetEntrenoFromRealiza entreno,
        GetUsuarioFromRealiza usuario
) {

    public static GetRealizaDto of(Realiza realiza) {
        return new GetRealizaDto(
                realiza.isRealizado(),
                realiza.getImagen(),
                GetEntrenoFromRealiza.of(realiza.getEntrenamiento()),
                GetUsuarioFromRealiza.of(realiza.getUsuario())
        );
    }
}
