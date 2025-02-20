package com.salesianos.FitQuestPrototype.Entrenamiento.Model;

import jakarta.persistence.*;
import lombok.*;




import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "ejercicio")
public class Ejercicio {

    @Id
    @GeneratedValue
    private Long id;

    private String nombre;
    private String descripcion;
    private double series;
    private double repeticiones;
    private double duracion;

    private String urlImagenes;

    @ManyToMany(mappedBy = "ejercicios", fetch = FetchType.EAGER)
    @Builder.Default
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    private Set<Entrenamiento> entrenamientos = new HashSet<> ();

}
