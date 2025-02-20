package com.salesianos.FitQuestPrototype.User.Error;

public class ActivationExpiredException extends RuntimeException {
    public ActivationExpiredException(String s) {
        super(s);
    }
}
