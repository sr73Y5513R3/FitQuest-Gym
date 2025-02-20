package com.salesianos.FitQuestPrototype.Entrenamiento.Controllers;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.CreateEntrenoCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.GetEntrenamientoDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.GetEntrenoConEjercicioDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Services.EntrenamientoService;
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
@RequestMapping("/entrenamiento")
public class EntrenamientoController {

    private final EntrenamientoService entrenamientoService;


    @Operation(summary = "Obtiene todas los entrenamientos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado todas los entrenamientos",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetEntrenoConEjercicioDto.class)))}),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado entrenamientos",
                    content = @Content)
    })
    @GetMapping("/all")
    public List<GetEntrenoConEjercicioDto> findAllEntrenamientos(){
        return entrenamientoService.findAllEntrenamientos()
                .stream()
                .map(GetEntrenoConEjercicioDto::of)
                .toList();
    }

    @Operation(summary = "Crea un entrenamiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Entrenamiento creado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetEntrenamientoDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para crear un entrenamiento",
                    content = @Content)
    })
    @PostMapping("/add")
    public ResponseEntity<GetEntrenamientoDto> save (@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo del entrenamiento", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateEntrenoCmd.class),
                    examples = @ExampleObject(value = """
                                                     {
                                                          "nombre": "Electrónica"
                                                      }
                            """)))@RequestBody CreateEntrenoCmd createEntrenoCmd) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetEntrenamientoDto.of(entrenamientoService.save(createEntrenoCmd)));
    }

    @Operation(summary = "Añade un ejercicio a un entrenamiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha añadido correctamente el ejercicio",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetEntrenoConEjercicioDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Alguno de los id son inválidos",
                    content = @Content)
    })
    @PostMapping("{idEntrenamiento}/ejercicio/{idEjercicio}")
    public ResponseEntity<GetEntrenoConEjercicioDto> addEjercicio (@PathVariable Long idEntrenamiento, @PathVariable Long idEjercicio) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetEntrenoConEjercicioDto.of(entrenamientoService.añadirEjercicio(idEntrenamiento, idEjercicio)));
    }

}
