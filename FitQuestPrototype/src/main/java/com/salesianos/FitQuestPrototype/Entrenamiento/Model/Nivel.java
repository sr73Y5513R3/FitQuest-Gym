package com.salesianos.FitQuestPrototype.Entrenamiento.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name= "nivel")
public class Nivel {

    @Id
    @GeneratedValue
    private Long id;

    private String nombre;

    @OneToMany(mappedBy = "nivel", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Entrenamiento> entrenamientos = new ArrayList<>();

    @OneToMany(mappedBy = "nivel", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Ejercicio> ejercicios = new ArrayList<>();

    //Metodos helpers

    //Entrenamiento

    public void addEntrenamiento(Entrenamiento entrenamiento) {
        this.entrenamientos.add(entrenamiento);
        entrenamiento.setNivel(this);
    }

    public void deleteEntrenamiento(Entrenamiento entrenamiento) {
        entrenamiento.setNivel(null);
        this.entrenamientos.remove(entrenamiento);
    }

    //Ejercicio

    public void addEjercicio(Ejercicio ejercicio) {
        this.ejercicios.add(ejercicio);
        ejercicio.setNivel(this);
    }

    public void deleteEjercicio(Ejercicio ejercicio) {
        ejercicio.setNivel(null);
        this.ejercicios.remove(ejercicio);
    }
}
