package com.salesianos.FitQuestPrototype.User.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
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

    private Genero genero;

    private Mensualidad mensualidad;

    private double puntos;
}
