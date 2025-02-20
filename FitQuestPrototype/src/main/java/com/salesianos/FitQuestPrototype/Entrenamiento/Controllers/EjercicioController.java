package com.salesianos.FitQuestPrototype.Entrenamiento.Controllers;


import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio.CreateEjercicioCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio.GetEjercicioDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Services.EjercicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ejercicio")
public class EjercicioController {

    private final EjercicioService ejercicioService;

    @PostMapping("/add")
    public ResponseEntity<GetEjercicioDto> addEjercicio(@RequestBody CreateEjercicioCmd createEjercicioCmd) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetEjercicioDto.of(ejercicioService.save(createEjercicioCmd)));
    }
}
