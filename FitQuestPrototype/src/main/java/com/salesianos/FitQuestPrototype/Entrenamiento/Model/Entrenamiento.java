package com.salesianos.FitQuestPrototype.Entrenamiento.Model;

import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
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

    @ManyToMany
    @JoinTable(name = "entrenamiento_ejercicio",
    joinColumns = @JoinColumn(name = "entrenamiento_id"),
    inverseJoinColumns = @JoinColumn(name= "ejercicio_id"),
            foreignKey = @ForeignKey(name = "fk_entrenamiento_ejercicio_entrenamiento"),
            inverseForeignKey = @ForeignKey(name = "fk_entrenamiento_ejercicio_ejercicio"))
    @Builder.Default
    private Set<Ejercicio> ejercicios = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "nivel_id",
        foreignKey = @ForeignKey(name = "fk_entrenamiento_nivel"))
    @ToString.Exclude
    private Nivel nivel;

    @OneToMany(mappedBy = "entrenamiento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Realiza> realizados = new ArrayList<>();

    //Métodos helpers

    public void addEjercicio(Ejercicio ejercicio) {
        if (!this.ejercicios.contains(ejercicio)) {
            this.ejercicios.add(ejercicio);
            ejercicio.addEntrenamiento(this);
        }
    }


    public void removeEjercicio(Ejercicio ejercicio) {
        if(this.ejercicios.contains(ejercicio)) {
            this.getEjercicios().remove(ejercicio);
            ejercicio.removeEntrenamiento(this);
        }
    }


}
