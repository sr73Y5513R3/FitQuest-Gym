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
@Table(name= "entrenador")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Entrenador extends Usuario {
}
