package com.salesianos.protoProyectoFitQuest.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name= "usuario")
public class Usuario {

    @Id
    @GeneratedValue
    private Long id;

    private String nombre;

    private String apellido1;
    private String apellido2;

    private String email;

    private String username;
    private String password;

}
