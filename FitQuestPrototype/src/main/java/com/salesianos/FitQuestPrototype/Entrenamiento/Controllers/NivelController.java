package com.salesianos.FitQuestPrototype.Entrenamiento.Controllers;

import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Ejercicio.GetEjercicioDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Entrenamiento.GetEntrenoConEjercicioDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Material.CreateMateriaCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Nivel.CreateNivelCmd;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Nivel.GetNivelConEjercicioDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Nivel.GetNivelConEntrenoDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Dto.Nivel.GetNivelDto;
import com.salesianos.FitQuestPrototype.Entrenamiento.Model.Nivel;
import com.salesianos.FitQuestPrototype.Entrenamiento.Services.NivelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/nivel")
public class NivelController {

    private final NivelService nivelService;

    @Operation(summary = "Obtiene todos los niveles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado todos los niveles",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetNivelConEntrenoDto.class)))}),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado niveles",
                    content = @Content)
    })
    @GetMapping("/all")
    public Page<GetNivelConEjercicioDto> getAllNivels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return nivelService.findAllNiveles(pageable)

                .map(GetNivelConEjercicioDto::of);

    }

    @Operation(summary = "Obtiene un nivel buscado por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado el nivel con ese id",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetNivelConEntrenoDto.class)))}),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado niveles con ese id",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public GetNivelConEntrenoDto getNivelById(@PathVariable Long id) {
        return GetNivelConEntrenoDto.of(nivelService.findNivelById(id));
    }

    @Operation(summary = "Crea un nivel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Nivel creado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetNivelDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para crear un nivel",
                    content = @Content)
    })
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GetNivelDto> addNivel(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo del nivel", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateNivelCmd.class),
                    examples = @ExampleObject(value = """
                                                     {
                                                          "nombre": "Principiante"
                                                      }
                            """)))@RequestBody @Valid CreateNivelCmd newNivel) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GetNivelDto.of(nivelService.save(newNivel)));
    }

    @Operation(summary = "Edita un nivel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Nivel editado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetNivelDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para editar un nivel",
                    content = @Content)
    })
    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public GetNivelDto editNivel(@PathVariable Long id, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo del nivel", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateNivelCmd.class),
                    examples = @ExampleObject(value = """
                                                     {
                                                          "nombre": "Profesional"
                                                      }
                            """)))@RequestBody @Valid CreateNivelCmd editNivel) {
        return GetNivelDto.of(nivelService.edit(id, editNivel));
    }

    @Operation(summary = "Añade un entrenamiento a un nivel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Entrenamiento añadido con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetNivelDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para añadir un entrenamiento a un nivel",
                    content = @Content)
    })
    @PostMapping("/{idNivel}/entrenamiento/{idEntreno}")
    @PreAuthorize("@entrenamientoService.isEntrenador(#idEntrenamiento)")
    public GetNivelConEntrenoDto addEntreno (@PathVariable Long idNivel, @PathVariable Long idEntreno) {
        return GetNivelConEntrenoDto.of(nivelService.addEntrenamiento(idNivel, idEntreno));
    }

    @Operation(summary = "Añade un ejercicio a un nivel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Ejercicio añadido con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetNivelDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos para añadir un ejercicio a un nivel",
                    content = @Content)
    })
    @PostMapping("/{idNivel}/ejercicio/{idEjercicio}")
    @PreAuthorize("hasRole('ADMIN')")
    public GetNivelConEjercicioDto addEjercicio (@PathVariable Long idNivel, @PathVariable Long idEjercicio) {
        return GetNivelConEjercicioDto.of(nivelService.addEjercicio(idNivel, idEjercicio));
    }

    @Operation(summary = "Elimina un nivel ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Nivel eliminado con éxito",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado el nivel con ese id",
                    content = @Content)
    })
    @DeleteMapping("/delete/{idNivel}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteNivel(@PathVariable Long idNivel) {
        nivelService.borrarNivel(idNivel);
        return ResponseEntity.noContent().build();
    }
}
