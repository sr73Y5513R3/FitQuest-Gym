package com.salesianos.FitQuestPrototype.Entrenamiento.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "material")
public class Material {

    @Id
    @GeneratedValue
    private Long id;

    private String nombre;
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;




}
