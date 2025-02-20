package com.salesianos.FitQuestPrototype.Entrenamiento.Controllers;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.CreateEntrenoCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.GetEntrenamientoDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Services.EntrenamientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/entrenamiento")
public class EntrenamientoController {

    private final EntrenamientoService entrenamientoService;


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


}
