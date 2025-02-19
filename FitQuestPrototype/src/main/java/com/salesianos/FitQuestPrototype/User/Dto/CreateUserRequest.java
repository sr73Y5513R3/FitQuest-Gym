package com.salesianos.FitQuestPrototype.User.Dto;

public record CreateUserRequest(
    String nombre,
    String apellido1,
    String apellido2,
    String email,
    String username,
    String password,
    String verifyPassword
){
}
