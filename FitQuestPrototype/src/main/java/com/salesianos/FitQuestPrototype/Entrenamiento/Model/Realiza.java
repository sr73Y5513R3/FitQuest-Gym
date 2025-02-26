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
@Table(name = "realiza")
@IdClass(RealizaID.class)
public class Realiza {

    private boolean realizado;

    private String imagen;

    @Id
    @ManyToOne
    @JoinColumn(name = "idEntrenamiento", foreignKey = @ForeignKey(name = "fk_realiza_entrenamiento"))
    private Entrenamiento entrenamiento;

    @Id
    @ManyToOne
    @JoinColumn(name = "idUsuario", foreignKey = @ForeignKey(name = "fk_realiza_usuario"))
    private Usuario usuario;

}
