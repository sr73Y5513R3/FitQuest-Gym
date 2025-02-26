package com.salesianos.FitQuestPrototype.User.Error;

public class EqualLevelException extends RuntimeException {

    public EqualLevelException() {
        super("No puedes cambiar el nivel porque es el mismo al del usuario");
    }


    public EqualLevelException(String message) {
        super(message);
    }


    public EqualLevelException(String message, Throwable cause) {
        super(message, cause);
    }


    public EqualLevelException(Throwable cause) {
        super(cause);
    }
}
