package com.salesianos.FitQuestPrototype.Entrenamiento.Model;

import com.salesianos.FitQuestPrototype.User.Model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValoracionId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Entrenamiento entrenamientoValorado;
    private Usuario usuarioValorar;
}
