package com.salesianos.FitQuestPrototype.Entrenamiento.Model;

import com.salesianos.FitQuestPrototype.User.Model.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "valoracion")
@IdClass(ValoracionId.class)
public class Valoracion {

    @Id
    @ManyToOne
    @JoinColumn(name = "idEntrenamiento", foreignKey = @ForeignKey(name = "fk_valoracion_entrenamiento"))
    private Entrenamiento entrenamientoValorado;

    @Id
    @ManyToOne
    @JoinColumn(name = "idUsuario", foreignKey = @ForeignKey(name = "fk_valoracion_usuario"))
    private Usuario usuarioValorar;

    private double notaValoracion;

    //private String textoDescriptivo;
}
