package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Valoracion;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Valoracion;

public record GetValoracionDto(
        GetUsuarioFromValorar usuario,
        GetEntrenoFromValorar entrenamiento,
        double notaValoracion,
        String textoDescriptivo
) {

    public static GetValoracionDto of (Valoracion valoracion){
        return new GetValoracionDto(
                GetUsuarioFromValorar.of(valoracion.getUsuarioValorar()),
                GetEntrenoFromValorar.of(valoracion.getEntrenamientoValorado()),
                valoracion.getNotaValoracion(),
                valoracion.getTextoDescriptivo()
        );
    }
}
