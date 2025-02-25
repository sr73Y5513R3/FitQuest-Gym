package com.salesianos.FitQuestPrototype.User.Model;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name= "cliente")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Cliente extends Usuario {

    private double peso;
    private double altura;
    private double edad;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Enumerated(EnumType.STRING)
    private Mensualidad mensualidad;

    private double puntos;

    @ManyToOne
    @JoinColumn(name = "nivel_id",
    foreignKey = @ForeignKey(name = "fk_cliente_nivel"))
    private Nivel nivel;
}
