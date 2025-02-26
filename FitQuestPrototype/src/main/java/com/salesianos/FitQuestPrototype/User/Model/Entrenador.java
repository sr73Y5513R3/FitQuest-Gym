package com.salesianos.FitQuestPrototype.User.Model;

import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Entrenamiento;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name= "entrenador")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Entrenador extends Usuario {

    @OneToMany(mappedBy = "entrenador", fetch = FetchType.LAZY)
    private List<Entrenamiento> entrenamientos = new ArrayList<>();

    //Métodos helpers

    public void addEntrenamiento(Entrenamiento entrenamiento) {
        entrenamiento.setEntrenador(this);
        this.entrenamientos.add(entrenamiento);
    }

    public void removeEntrenamiento(Entrenamiento entrenamiento) {
        this.entrenamientos.remove(entrenamiento);
        entrenamiento.setEntrenador(null);
    }

}
