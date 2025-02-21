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

    @ManyToMany(mappedBy = "ejercicios", fetch = FetchType.LAZY)
    @Builder.Default
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    private Set<Entrenamiento> entrenamientos = new HashSet<> ();

    
    //Relación unidireccional

    @ManyToMany
    @JoinTable(name = "ejercicio_material",
            joinColumns = @JoinColumn(name = "ejercicio_id"),
            inverseJoinColumns = @JoinColumn(name= "material_id"),
            foreignKey = @ForeignKey(name = "fk_ejercicio_material_ejercicio"),
            inverseForeignKey = @ForeignKey(name = "fk_ejercicio_material_material"))
    @Builder.Default
    private Set<Material> materiales = new HashSet<Material> ();

    //Métodos helpers

    public void addEntrenamiento(Entrenamiento entrenamiento) {
        if (!this.entrenamientos.contains(entrenamiento)) {  // Evita la recursión infinita
            this.entrenamientos.add(entrenamiento);
            entrenamiento.addEjercicio(this);
        }
    }


    public void removeEntrenamiento(Entrenamiento entrenamiento) {
        this.getEntrenamientos().remove(entrenamiento);
        entrenamiento.removeEjercicio(this);
    }

}
