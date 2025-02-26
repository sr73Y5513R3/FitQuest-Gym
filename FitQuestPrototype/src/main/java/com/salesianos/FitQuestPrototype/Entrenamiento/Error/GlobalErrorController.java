package com.salesianos.FitQuestPrototype.Entrenamiento.Error;

import com.salesianos.FitQuestPrototype.User.Error.ActivationExpiredException;
import com.salesianos.FitQuestPrototype.User.Error.EqualLevelException;
import com.salesianos.FitQuestPrototype.User.Error.UserNotAuthorizedException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.logging.Logger;

@Log
@RestControllerAdvice
public class GlobalErrorController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntidadNoEncontradaException.class)
    public ProblemDetail handleEjercicioNotFoundException(EntidadNoEncontradaException ex) {
        Logger.getLogger(GlobalErrorController.class.getName()).severe("Error: " + ex.getMessage());
        ProblemDetail result = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        result.setTitle("Entidad no encontrada");
        result.setType(URI.create("https://www.salesianos-triana.edu/errors/entity-not-found"));
        result.setProperty("author", "pabloTey");
        return result;
    }

    @ExceptionHandler(ActivationExpiredException.class)
    public ProblemDetail handleActivationExpiredException(ActivationExpiredException ex) {
        Logger.getLogger(GlobalErrorController.class.getName()).severe("Error: " + ex.getMessage());
        ProblemDetail result = ProblemDetail.forStatusAndDetail(HttpStatus.GONE, ex.getMessage());
        result.setTitle("Activación expirada");
        return result;
    }

    @ExceptionHandler(EqualLevelException.class)
    public ProblemDetail handleEqualLevelException(EqualLevelException ex) {
        Logger.getLogger(GlobalErrorController.class.getName()).severe("Error: " + ex.getMessage());
        ProblemDetail result = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        result.setTitle("Nivel no cambiado");
        return result;
    }

    @ExceptionHandler(UserNotAuthorizedException.class)
    public ProblemDetail handleUserNotAuthorizedException(UserNotAuthorizedException ex) {
        Logger.getLogger(GlobalErrorController.class.getName()).severe("Error: " + ex.getMessage());
        ProblemDetail result = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        result.setTitle("Usuario no autorizado");
        return result;
    }

    @ExceptionHandler(EntidadYaAñadidaException.class)
    public ProblemDetail handleEntidadYaAñadidaException(EntidadYaAñadidaException ex){
        Logger.getLogger(GlobalErrorController.class.getName()).severe("Error: " + ex.getMessage());
        ProblemDetail result = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        result.setTitle("Entidad ya añadida a la lista");
        return result;
    }

    @ExceptionHandler(NoContainsException.class)
    public ProblemDetail handleNoContainsException(NoContainsException ex) {
        Logger.getLogger(GlobalErrorController.class.getName()).severe("Error: " + ex.getMessage());
        ProblemDetail result = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        result.setTitle("Lista vacia");
        return result;
    }

    @ExceptionHandler(BorradoIlegalException.class)
    public ProblemDetail handleBorradoIlegalException(BorradoIlegalException ex){
        Logger.getLogger(GlobalErrorController.class.getName()).severe("Error: " + ex.getMessage());
        ProblemDetail result = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        result.setTitle("Borrado illegal");
        return result;
    }



}
