package com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Tipo;

public record CreateMateriaCmd (
        String nombre,
        String descripcion,
        Tipo tipo
){
}
