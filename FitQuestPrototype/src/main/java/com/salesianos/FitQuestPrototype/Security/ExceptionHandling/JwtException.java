package com.salesianos.FitQuestPrototype.Security.ExceptionHandling;

public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}
