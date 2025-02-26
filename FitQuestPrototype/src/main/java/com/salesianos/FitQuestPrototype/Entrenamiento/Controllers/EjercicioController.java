package com.salesianos.FitQuestPrototype.Entrenamiento.Controllers;


import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio.CreateEjercicioCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio.GetEjercicioConNivelDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio.GetEjercicioDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.CreateEntrenoCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.GetEntrenamientoDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.GetEntrenoConEjercicioDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.GetEntrenoConNivelDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Services.EjercicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
                    description = "Se han encontrado todos los ejercicios",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetEjercicioDto.class)))}),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado ejercicios",
                    content = @Content)
    })
    @GetMapping("/all")
    public Page<GetEjercicioDto> findAllEjercicios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return ejercicioService.findAllEjercicio(pageable)
                .map(GetEjercicioDto::of);
    }


    @Operation(summary = "Busca un ejercicio por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado el ejercicio buscado",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetEjercicioDto.class)))}),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado ejercicios con ese id",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public GetEjercicioDto findEjercicioById(@PathVariable Long id){
        return GetEjercicioDto.of(ejercicioService.findEjercicioById(id));
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
                            """)))@RequestBody @Valid CreateEjercicioCmd createEjercicioCmd){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetEjercicioDto.of(ejercicioService.save(createEjercicioCmd)));
    }

    @Operation(summary = "Edita un ejercicio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Ejercicio editado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetEjercicioDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para editar el ejercicio",
                    content = @Content)
    })
    @PutMapping("/edit/{id}")
    public GetEjercicioDto editEjercicio(@PathVariable Long id,@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo del ejercicio", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateEjercicioCmd.class),
                    examples = @ExampleObject(value = """
                                                     {
                                                          "nombre": "Electrónica"
                                                      }
                            """))) @RequestBody @Valid CreateEjercicioCmd createEjercicioCmd){
        return GetEjercicioDto.of(ejercicioService.edit(id, createEjercicioCmd));
    }

    @Operation(summary = "Edita el nivel de un ejercicio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha actualizado correctamente el nivel",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetEntrenoConNivelDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Alguno de los id son inválidos",
                    content = @Content)
    })
    @PostMapping("/{id}/editNivel/{idNivel}")
    public GetEjercicioConNivelDto editNivel (@PathVariable Long id, @PathVariable Long idNivel) {
        return GetEjercicioConNivelDto.of(ejercicioService.actualizarNivel(id, idNivel));
    }

    @Operation(summary = "Obtiene un ejercicio buscado por nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha encontrado un ejercicio con ese nombre",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetEjercicioDto.class)))}),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado ejercicios con ese nombre",
                    content = @Content)
    })
    @GetMapping("/nombre")
    public GetEjercicioDto findByNombre(@RequestParam @NotBlank(message = "El nombre no puede estar vacío")
                                            @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
                                            String nombre){
        return GetEjercicioDto.of(ejercicioService.findByNombre(nombre));
    }
}
