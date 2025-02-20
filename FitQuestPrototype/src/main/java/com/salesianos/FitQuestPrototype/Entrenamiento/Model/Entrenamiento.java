package com.salesianos.FitQuestPrototype.Entrenamiento.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "entrenamiento")
public class Entrenamiento {

    @Id
    @GeneratedValue
    Long id;

    String nombre;
    String descripcion;
    double duracion;
    double calorias;
    double puntos;
    String autor;

    //double valoracionMedia
}
