package com.salesianos.FitQuestPrototype.Entrenamiento.Controllers;


import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio.CreateEjercicioCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio.GetEjercicioDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.CreateEntrenoCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.GetEntrenamientoDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.GetEntrenoConEjercicioDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Services.EjercicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ejercicio")
public class EjercicioController {

    private final EjercicioService ejercicioService;

    @Operation(summary = "Obtiene todas los ejercicios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado todas los ejercicios",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetEjercicioDto.class)))}),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado ejercicios",
                    content = @Content)
    })
    @GetMapping("/all")
    public List<GetEjercicioDto> findAllEjercicios(){
        return ejercicioService.findAllEjercicio()
                .stream()
                .map(GetEjercicioDto::of)
                .toList();
    }


    @Operation(summary = "Crea un ejercicio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Ejercicio creado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetEjercicioDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para crear un ejercicio",
                    content = @Content)
    })
    @PostMapping("/add")
    public ResponseEntity<GetEjercicioDto> addEjercicio(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo del ejercicio", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateEjercicioCmd.class),
                    examples = @ExampleObject(value = """
                                                     {
                                                          "nombre": "Electrónica"
                                                      }
                            """)))@RequestBody CreateEjercicioCmd createEjercicioCmd){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetEjercicioDto.of(ejercicioService.save(createEjercicioCmd)));
    }
}
